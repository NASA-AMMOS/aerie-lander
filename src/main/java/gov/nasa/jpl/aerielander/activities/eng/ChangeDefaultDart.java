package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports ChangeDefaultDART.
 */
@ActivityType("ChangeDefaultDart")
public final class ChangeDefaultDart {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public DataConfig.DART dartConfig = DataConfig.DART.DEFAULT;

  public ChangeDefaultDart() { }

  public ChangeDefaultDart(final DataConfig.DART dartConfig) {
    this.dartConfig = dartConfig;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    mission.dataModel.defaultDART.set(dartConfig);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
