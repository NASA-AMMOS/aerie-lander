package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports SEIS_CHANGE_SP_CFG.
 */
@ActivityType("SeisChangeSPConfig")
public final class SeisChangeSPConfig {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public SeisConfig.Gain spGain = SeisConfig.Gain.HIGH;

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    final var model = mission.seisModel;

    model.setGain(SeisConfig.DeviceType.SP, spGain);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
