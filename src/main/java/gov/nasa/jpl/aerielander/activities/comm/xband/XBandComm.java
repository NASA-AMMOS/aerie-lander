package gov.nasa.jpl.aerielander.activities.comm.xband;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.comm.CommModel.XBandAntenna;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.defer;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.comm.CommModel.XBandAntenna.EAST_MGA;

@ActivityType("XBandComm")
public final class XBandComm {
  @Parameter public String mnemonic = "";
  @Parameter public Duration xbandTxDuration = Duration.of(10, MINUTES);
  @Parameter public Duration initialRtOnlyDuration = Duration.of(5, MINUTES);
  @Parameter public Duration delayToTxOn = Duration.of(5, MINUTES);
  @Parameter public XBandAntenna xbandAntSel = EAST_MGA;
  @Parameter public String dlRate = "1400_BPS";
  @Parameter public String ulRate = "1000_BPS";
  @Parameter public double dataVolMbits = 0.0;
  @Parameter public String DSNTrack = "DSS-25";

  @Validation("XBandComm xbandTxDuration cannot be less than 20 minutes or greater than 2 hours.")
  @Validation.Subject("xbandTxDuration")
  public boolean validateXbandTxDuration() {
    return xbandTxDuration.noShorterThan(Duration.of(20, MINUTES)) &&
           xbandTxDuration.noLongerThan(Duration.of(120, MINUTES));
  }

  @EffectModel
  public void run(final Mission mission) {
    final var commParameters = mission.config.commParameters();
    final var clock = mission.clocks;
    final var start = clock.getCurrentTime();
    final var duration = commParameters.XBAND_PREP_OVERHEAD().plus(delayToTxOn).plus(xbandTxDuration).plus(commParameters.XBAND_CLEANUP_DURATION());
    final var end = start.plus(duration);
    final var prepDuration = delayToTxOn.plus(commParameters.XBAND_PREP_OVERHEAD());
    final var cleanupStart = end.minus(commParameters.XBAND_CLEANUP_DURATION());

    mission.commModel.setXBandAntenna(xbandAntSel);

    // XBAND_SATF is for sequence generation, so we won't translate that
    //spawn(new XBandSATF(name, xbandTxDuration, initialRtOnlyDuration, delayToTxOn, xbandAntSel, dlRate, ulRate));
    spawn(mission, new XBandPrep(prepDuration));
    defer(delayToTxOn, mission, new XBandActive(xbandTxDuration));
    defer(clock.timeUntil(cleanupStart), mission, new XBandCleanup(commParameters.XBAND_CLEANUP_DURATION()));
  }
}
