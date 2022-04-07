package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;

/**
 * Effectively supports SEIS_CHANGE_VBB_CFG.
 */
@ActivityType("SeisChangeVBBConfig")
public final class SeisChangeVBBConfig {

  @Parameter
  public Duration duration = Duration.of(3, MINUTES);

  @Parameter
  public SeisConfig.VBBMode vbbMode = SeisConfig.VBBMode.SCI;

  @Parameter
  public SeisConfig.Gain velGain = SeisConfig.Gain.HIGH;

  @Parameter
  public SeisConfig.Gain posGain = SeisConfig.Gain.HIGH;

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    final var model = mission.seisModel;

    model.vbbMode.set(vbbMode);
    model.setGain(SeisConfig.DeviceType.VEL, velGain);
    model.setGain(SeisConfig.DeviceType.POS, posGain);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
