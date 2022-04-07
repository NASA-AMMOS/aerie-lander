package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports SEIS_CHANGE_DATA_PROCESS_CFG.
 */
@ActivityType("SeisChangeDataProcessConfig")
public final class SeisChangeDataProcessConfig {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public Map<SeisConfig.Channel, SeisConfig.ChannelRate> channelRates = new HashMap<>();

  @Parameter
  public Map<List<SeisConfig.Channel>, Double> combinedChannelOutRates = new HashMap<>();

  @Parameter
  public double transferCoef = 1 / 2.16; // Mbit/s

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    final var model = mission.seisModel;

    model.transferRate.set(transferCoef);
    model.setChannelRates(channelRates);

    model.setCombinedChannelOutRates(
        combinedChannelOutRates.entrySet().stream()
            .map(e -> new SeisConfig.ChannelOutRateGroup(e.getValue(), e.getKey()))
            .collect(Collectors.toList()));

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
