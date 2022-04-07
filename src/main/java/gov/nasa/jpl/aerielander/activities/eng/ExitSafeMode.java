package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.eng.EngModel.Component;

/**
 * Effectively supports ExitSafeMode.
 */
@ActivityType("ExitSafeMode")
public final class ExitSafeMode {

  @Parameter
  public Duration duration = MINUTE;

  @Parameter
  public Component component = Component.Lander;

  public ExitSafeMode() { }

  public ExitSafeMode(final Component component) {
    this.component = component;
  }

  public ExitSafeMode(final Duration duration, final Component component) {
    this(component);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    exitSafeMode(mission);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }

  private void exitSafeMode(final Mission mission) {
    if (component == Component.Lander) {
      // Set heaters back to normal
      spawn(new ToggleTeHeaters.Builder()
                .withDuration(duration)
                .withSurvivalPrimaryDisabled(true)
                .withSurvivalSecondaryDisabled(true)
                .build());
    }
    mission.engModel.setSafeMode(component, false);
  }
}
