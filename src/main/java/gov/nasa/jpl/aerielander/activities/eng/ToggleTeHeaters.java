package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;

/**
 * Effectively supports ToggleTEHeaters.
 */
@ActivityType("ToggleTeHeaters")
public final class ToggleTeHeaters {
  private static final Duration DEFAULT_DURATION = MINUTE;
  private static final boolean DEFAULT_DISABLED = false;

  @Parameter
  public Duration duration = DEFAULT_DURATION;

  @Parameter
  public boolean nominalPrimaryDisabled = DEFAULT_DISABLED;

  @Parameter
  public boolean nominalSecondaryDisabled = DEFAULT_DISABLED;

  @Parameter
  public boolean survivalPrimaryDisabled = DEFAULT_DISABLED;

  @Parameter
  public boolean survivalSecondaryDisabled = DEFAULT_DISABLED;

  public ToggleTeHeaters() { }

  private ToggleTeHeaters(
      final Duration duration,
      final boolean nominalPrimaryDisabled,
      final boolean nominalSecondaryDisabled,
      final boolean survivalPrimaryDisabled,
      final boolean survivalSecondaryDisabled)
  {
    this.duration = duration;
    this.nominalPrimaryDisabled = nominalPrimaryDisabled;
    this.nominalSecondaryDisabled = nominalSecondaryDisabled;
    this.survivalPrimaryDisabled = survivalPrimaryDisabled;
    this.survivalSecondaryDisabled = survivalSecondaryDisabled;
  }

  public static final class Builder {
    private Duration duration = DEFAULT_DURATION;
    private boolean nominalPrimaryDisabled = DEFAULT_DISABLED;
    private boolean nominalSecondaryDisabled = DEFAULT_DISABLED;
    private boolean survivalPrimaryDisabled = DEFAULT_DISABLED;
    private boolean survivalSecondaryDisabled = DEFAULT_DISABLED;

    public Builder withDuration(final Duration duration) {
      this.duration = duration;
      return this;
    }

    public Builder withNominalPrimaryDisabled(final boolean disabled) {
      this.nominalPrimaryDisabled = disabled;
      return this;
    }

    public Builder withNominalSecondaryDisabled(final boolean disabled) {
      this.nominalSecondaryDisabled = disabled;
      return this;
    }

    public Builder withSurvivalPrimaryDisabled(final boolean disabled) {
      this.survivalPrimaryDisabled = disabled;
      return this;
    }

    public Builder withSurvivalSecondaryDisabled(final boolean disabled) {
      this.survivalSecondaryDisabled = disabled;
      return this;
    }

    public ToggleTeHeaters build() {
      return new ToggleTeHeaters(
          duration,
          nominalPrimaryDisabled,
          nominalSecondaryDisabled,
          survivalPrimaryDisabled,
          survivalSecondaryDisabled);
    }
  }

  @EffectModel
  public void run(final Mission mission) {
    // TODO stubbed for now, implement this to match ENG_activities.aaf ToggleTEHeaters modeling
    final var end = mission.clocks.getCurrentTime().plus(duration);
//    mmpatSetParamData(
//      "",
//      "thrm_stat_disabled_list",
//      nominalPrimaryDisabled,
//      nominalSecondaryDisabled,
//      survivalPrimaryDisabled,
//      survivalSecondaryDisabled);
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
