package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports ChangeDefaultFPT.
 */
@ActivityType("ChangeDefaultFpt")
public final class ChangeDefaultFpt {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public DataConfig.FPT fptConfig = DataConfig.FPT.DEFAULT;

  public ChangeDefaultFpt() { }

  public ChangeDefaultFpt(final DataConfig.FPT fptConfig) {
    this.fptConfig = fptConfig;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    mission.dataModel.defaultFPT.set(fptConfig);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
