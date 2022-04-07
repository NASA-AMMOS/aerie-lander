package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports UpdateFPT.
 * This activity is used to tactically update the FPT.
 */
@ActivityType("UpdateFpt")
public final class UpdateFpt {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public DataConfig.FPT fptConfig = DataConfig.FPT.dwn_fpt_surface_nom;

  public UpdateFpt() { }

  public UpdateFpt(final DataConfig.FPT fptConfig) {
    this.fptConfig = fptConfig;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    mission.dataModel.updateFPT(fptConfig);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
