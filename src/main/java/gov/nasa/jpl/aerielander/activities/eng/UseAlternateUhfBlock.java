package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.comm.CommModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports UseAlternateUhfBlock.
 */
@ActivityType("UseAlternateUhfBlock")
public final class UseAlternateUhfBlock {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @Parameter
  public CommModel.Orbiter orbiter = CommModel.Orbiter.ODY;

  @Parameter
  public boolean enableAlternateBlock = false;

  public UseAlternateUhfBlock() { }

  public UseAlternateUhfBlock(final CommModel.Orbiter orbiter, boolean enableAlternateBlock) {
    this.orbiter = orbiter;
    this.enableAlternateBlock = enableAlternateBlock;
  }

  public UseAlternateUhfBlock(final Duration duration, final CommModel.Orbiter orbiter, boolean enableAlternateBlock) {
    this(orbiter, enableAlternateBlock);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    mission.commModel.setAlternateUhfBlockInUse(orbiter, enableAlternateBlock);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
