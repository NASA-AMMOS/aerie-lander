package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.activities.apss.APSSChangeAcqConfig;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeBeeOff;
import gov.nasa.jpl.aerielander.activities.ids.ICCHeatersOff;
import gov.nasa.jpl.aerielander.activities.ids.IDCHeatersOff;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOff;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.eng.EngModel.Component;

/**
 * Effectively supports EnterSafeMode.
 */
@ActivityType("EnterSafeMode")
public final class EnterSafeMode {

  @Parameter
  public Duration duration = MINUTE;

  @Parameter
  public Component component = Component.Lander;

  public EnterSafeMode() { }

  public EnterSafeMode(final Component component) {
    this.component = component;
  }

  public EnterSafeMode(final Duration duration, final Component component) {
    this(component);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    enterSafeMode(mission);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }

  private void enterSafeMode(final Mission mission) {
    switch (component) {
      case Lander -> {
        spawn(makeApssAllOff());
        spawn(new SeisPowerOff(duration));
        spawn(new HeatProbeBeeOff(duration));
        spawn(new ICCHeatersOff(duration));
        spawn(new IDCHeatersOff(duration));
        // Go to only survival heater (disable nominals)
        spawn(new ToggleTeHeaters.Builder()
            .withDuration(duration)
            .withNominalPrimaryDisabled(true)
            .withNominalSecondaryDisabled(true)
            .build());
      }
      case APSS -> spawn(makeApssAllOff());
      case SEIS -> spawn(new SeisPowerOff(duration));
      case HeatProbe -> spawn(new HeatProbeBeeOff(duration));
      case IDS -> {
        spawn(new ICCHeatersOff(duration));
        spawn(new IDCHeatersOff(duration));
      }
    }
    mission.engModel.setSafeMode(component, true);
  }

  private APSSChangeAcqConfig makeApssAllOff() {
    return new APSSChangeAcqConfig.Builder()
        .withDuration(duration)
        .withPaeOn(false)
        .withTwinsPyOn(false)
        .withTwinsMyOn(false)
        .withPsOn(false)
        .withIfgOn(false)
        .with28vOn(false)
        .build();
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
