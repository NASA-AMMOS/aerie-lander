package gov.nasa.jpl.aerielander.activities.comm.uhf;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.config.OrbiterConfiguration;
import gov.nasa.jpl.aerielander.models.comm.CommModel.Orbiter;
import gov.nasa.jpl.aerielander.models.data.DataConfig.FPT;
import gov.nasa.jpl.aerielander.models.time.Time;

import java.util.ArrayList;
import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MICROSECONDS;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.VC00;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.Harness;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.UHF_Lander;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.UHF_RF;

@ActivityType("UHFActive")
public final class UHFActive {
  private static final double LIMIT_RESOLUTION = 0.0001;

  @Parameter
  public String overflightID = "";

  @Parameter
  public double info_Mbits = 0.0; // expected data volume in Mbits that can be downlinked through this activity

  @Parameter
  public Duration iv_sci_dur_2 = Duration.ZERO;

  public UHFActive() {}

  public UHFActive(final String overflightID, final double info_Mbits, final Duration iv_sci_dur_2) {
    this.overflightID = overflightID;
    this.info_Mbits = info_Mbits;
    this.iv_sci_dur_2 = iv_sci_dur_2;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var clock = mission.clocks;
    final var start = clock.getCurrentTime();
    final var end = start.plus(iv_sci_dur_2);
    final var config = mission.config;
    final var dataModel = mission.dataModel;
    final var commModel = mission.commModel;
    final var powerModel = mission.powerModel;
    final var orbiter = getOrbiterFromOverflightID(overflightID);
    final var orbiterConfig = getOrbiterConfiguration(orbiter, config);
    final var uhfActiveDataRate = config.engDataParams().UHF_ACTIVE_DATA_RATE();
    final var half_iv_sci_dur_2 = Duration.of(iv_sci_dur_2.in(MICROSECONDS)/2, MICROSECONDS);
    final var iv_sci_dur_2_midpoint = start.plus(half_iv_sci_dur_2);
    final var delayForVC00 = orbiterConfig.delayForVC00();
    final var filteredFPT = getUhfVCsDownlinked(orbiter, dataModel.getCurrentFPT(), config, commModel.getAlternateUhfBlockInUseMap().get(orbiter).get());

    // Express duration in microseconds instead of hours for precision
    // Rate is in Mbit/s
    final var downlinkRate = (info_Mbits / iv_sci_dur_2.in(Duration.MICROSECOND)) * 1_000_000;

    // Add some extra data for the specific type of uhf block
    final var extraVol = orbiterConfig.dvAddedMbits();
    final var extraRate = extraVol / iv_sci_dur_2.in(SECONDS);

    // start collection of engineering telemetry
    final var totalActiveDataRate = uhfActiveDataRate + extraRate;
    dataModel.increaseDataRate(SPACECRAFT, totalActiveDataRate);

    powerModel.setPelState(UHF_Lander, "transmit");
    powerModel.setPelState(UHF_RF, "transmit");
    powerModel.setPelState(Harness, "uhf_transmit");

    // Downlink data
    // The downlink of data goes like this:
    //     Loop through the filteredFPT, downlinking data from the highest priority non-empty channel
    //     If delayForVC00 is set, skip VC00 whenever it appears prior to iv_sci_dur_2_midpoint
    //         at which point VC00 should be downlinked, and the filtered FPT continued thereafter
    //     If we get through the whole filtered FPT before the end of the pass, wait until there is
    //     data to downlink and repeat the process

    var downlinkCutoff = iv_sci_dur_2_midpoint;
    var ignoreList = (delayForVC00) ? List.of(VC00) : List.<ChannelName>of();
    int index = 0;
    while (clock.getCurrentTime().isBefore(end)) {
      var channel = filteredFPT.get(index);

      // If VC00 is to be held off until iv_sci_dur_2_midpoint, and it comes up prior, ignore it
      if (delayForVC00 && channel.equals(VC00) && clock.getCurrentTime().isBefore(iv_sci_dur_2_midpoint)) {
        index++;
      } else {

        if (clock.getCurrentTime().noEarlierThan(downlinkCutoff)) {
          if (downlinkCutoff.equals(iv_sci_dur_2_midpoint)) {
            ignoreList = List.of();
            index = 0;
            channel = filteredFPT.get(index);
            downlinkCutoff = end;
          } else {
            throw new RuntimeException("UHF Active model exceeded allocated duration");
          }
        }

        final var vc = dataModel.getChannel(channel);
        if (vc.volume.get() >= LIMIT_RESOLUTION) {
          final var dumpDuration = calculateDumpDuration(vc.volume.get(), downlinkRate, clock.getCurrentTime(), downlinkCutoff);
          vc.increaseDataRate(-downlinkRate);
          commModel.increaseDownlinkRate(downlinkRate);

          delay(dumpDuration);

          vc.increaseDataRate(downlinkRate);
          commModel.increaseDownlinkRate(-downlinkRate);

          // TODO: Image tracking logic goes here in the APGen mission model
          //       We need to address image tracking as well
        }

        if (vc.volume.get() < LIMIT_RESOLUTION) index++;
      }

      // If end of FPT reached, wait a little bit at a time until some data appears for downlink, or downlink_cutoff is reached
      if ( index == filteredFPT.size() ) {
        final var watchList = new ArrayList<>(filteredFPT);
        watchList.removeAll(ignoreList);
        final var dataVolCondition = dataModel.whenAnyVCExceedsThreshold(watchList, LIMIT_RESOLUTION);
        final var timeCondition = clock.whenTimeIs(end);
        waitUntil(dataVolCondition.or(timeCondition));
        index = 0;
      }
    }

    waitUntil(clock.whenTimeIs(end));

    // Unset UHF_ACTIVE_DATA_RATE
    dataModel.increaseDataRate(SPACECRAFT, -totalActiveDataRate);
  }

  private static OrbiterConfiguration getOrbiterConfiguration(final Orbiter orbiter, final Configuration config) {
    return switch (orbiter) {
      case ODY -> config.orbiterParams().ODY();
      case MRO -> config.orbiterParams().MRO();
      case TGO -> config.orbiterParams().TGO();
      case MVN -> config.orbiterParams().MVN();
      case MEX -> config.orbiterParams().MEX();
      default -> throw new RuntimeException(String.format("Unrecognized Orbiter \"%s\"", orbiter));
    };
  }

  private static Orbiter getOrbiterFromOverflightID(final String overflightID) {
    final var splitID = overflightID.split("_");
    if (splitID.length == 0) throw new RuntimeException(String.format("Overflight ID \"%s\" does not match expected format.", overflightID));

    final var orbiterName = splitID[0];
    return switch (orbiterName) {
      case "ODY" -> Orbiter.ODY;
      case "MRO" -> Orbiter.MRO;
      case "TGO" -> Orbiter.TGO;
      case "MVN" -> Orbiter.MVN;
      case "MEX" -> Orbiter.MEX;
      default -> throw new RuntimeException(String.format(
          "Unrecognized orbiter \"%s\" extracted from overflight ID \"%s\"",
          orbiterName,
          overflightID));
    };
  }

  private static List<ChannelName> getUhfVCsDownlinked(
      final Orbiter orbiter,
      final FPT fpt,
      final Configuration config,
      final boolean alternateUhfBlockInUse)
  {
    if (alternateUhfBlockInUse) {
      final var filteredPriorityList = new ArrayList<ChannelName>();
      final var orbiterVcsDownlinked = getOrbiterConfiguration(orbiter, config).vcsDownlinked();
      for (final var vc : fpt.order) {
        if (orbiterVcsDownlinked.contains(vc)) filteredPriorityList.add(vc);
      }
      return filteredPriorityList;
    }
    return fpt.order;
  }

  private static Duration calculateDumpDuration(final double volume, final double downlinkRate, final Time now, final Time maxTime) {
    return Duration.min(
        Duration.roundNearest(volume/downlinkRate, SECONDS),
        maxTime.minus(now));
  }
}
