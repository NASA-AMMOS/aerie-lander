package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import gov.nasa.jpl.aerielander.models.wake.WakeModel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem;

/**
 * Effectively supports APSS_CHANGE_ACQ_CONFIG.
 * Turns on or off different components of APSS. Keeping these continuous states correct is important for correct power and data modeling results.
 * Use this to represent most APSS activities except boom swaps.
 */
@ActivityType("APSSChangeAcqConfig")
public final class APSSChangeAcqConfig {
  private static final Duration DEFAULT_DURATION = Duration.of(10, MINUTES);
  private static final boolean DEFAULT_PAE_ON = true;
  private static final boolean DEFAULT_TWINS_PY_ON = false;
  private static final boolean DEFAULT_TWINS_MY_ON = true;
  private static final boolean DEFAULT_PS_ON = true;
  private static final boolean DEFAULT_IFG_ON = true;
  private static final boolean DEFAULT_28V_ON = true;
  private static final int DEFAULT_TWINS_PY_SETPOINT = -75; // Degrees C
  private static final int DEFAULT_TWINS_MY_SETPOINT = -75; // Degrees C

  @Parameter
  public Duration duration = DEFAULT_DURATION;

  @Parameter
  public boolean paeOn = DEFAULT_PAE_ON;

  @Parameter
  public boolean twinsPyOn = DEFAULT_TWINS_PY_ON;

  @Parameter
  public boolean twinsMyOn = DEFAULT_TWINS_MY_ON;

  @Parameter
  public boolean psOn = DEFAULT_PS_ON;

  @Parameter
  public boolean ifgOn = DEFAULT_IFG_ON;

  @Parameter
  public boolean _28vOn = DEFAULT_28V_ON;

  @Parameter
  public int twinsPySetpoint = DEFAULT_TWINS_PY_SETPOINT;

  @Parameter
  public int twinsMySetpoint = DEFAULT_TWINS_MY_SETPOINT;

  @Validation("TWINS PY heater setpoint must be within interval [-90, 50] (degrees C)")
  @Validation.Subject("twinsPySetpoint")
  public boolean validateTwinsPySetpoint() {
    return twinsPySetpoint >= -90 && twinsPySetpoint <= 50;
  }

  @Validation("TWINS MY heater setpoint must be within interval [-75, 50] (degrees C)")
  @Validation.Subject("twinsMySetpoint")
  public boolean validateTwinsMySetpoint() {
    return twinsMySetpoint >= -75 && twinsMySetpoint <= 50;
  }

  public APSSChangeAcqConfig() { }

  private APSSChangeAcqConfig(
      final Duration duration,
      final boolean paeOn,
      final boolean twinsPyOn,
      final boolean twinsMyOn,
      final boolean psOn,
      final boolean ifgOn,
      final boolean _28vOn,
      final int twinsPySetpoint,
      final int twinsMySetpoint)
  {
    this.duration = duration;
    this.paeOn = paeOn;
    this.twinsPyOn = twinsPyOn;
    this.twinsMyOn = twinsMyOn;
    this.psOn = psOn;
    this.ifgOn = ifgOn;
    this._28vOn = _28vOn;
    this.twinsPySetpoint = twinsPySetpoint;
    this.twinsMySetpoint = twinsMySetpoint;
  }

  public static final class Builder {
    private Duration duration = DEFAULT_DURATION;
    private boolean paeOn = DEFAULT_PAE_ON;
    private boolean twinsPyOn = DEFAULT_TWINS_PY_ON;
    private boolean twinsMyOn = DEFAULT_TWINS_MY_ON;
    private boolean psOn = DEFAULT_PS_ON;
    private boolean ifgOn = DEFAULT_IFG_ON;
    private boolean _28vOn = DEFAULT_28V_ON;
    private int twinsPySetpoint = DEFAULT_TWINS_PY_SETPOINT;
    private int twinsMySetpoint = DEFAULT_TWINS_MY_SETPOINT;

    public Builder withDuration(final Duration duration) {
      this.duration = duration;
      return this;
    }

    public Builder withPaeOn(final boolean on) {
      this.paeOn = on;
      return this;
    }

    public Builder withTwinsPyOn(final boolean on) {
      this.twinsPyOn = on;
      return this;
    }

    public Builder withTwinsMyOn(final boolean on) {
      this.twinsMyOn = on;
      return this;
    }

    public Builder withPsOn(final boolean on) {
      this.psOn = on;
      return this;
    }

    public Builder withIfgOn(final boolean on) {
      this.ifgOn = on;
      return this;
    }

    public Builder with28vOn(final boolean on) {
      this._28vOn = on;
      return this;
    }

    public Builder withTwinsPySetpoint(final int setpoint) {
      this.twinsPySetpoint = setpoint;
      return this;
    }

    public Builder withTwinsMySetpoint(final int setpoint) {
      this.twinsMySetpoint = setpoint;
      return this;
    }

    public APSSChangeAcqConfig build() {
      return new APSSChangeAcqConfig(
          duration,
          paeOn,
          twinsPyOn,
          twinsMyOn,
          psOn,
          ifgOn,
          _28vOn,
          twinsPySetpoint,
          twinsMySetpoint);
    }
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    // Toggle APSS HK data production
    final var hkChannel = mission.dataModel.hkModel.APSS;
    if (paeOn) {
      mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, hkChannel, hkChannel.defaultFullWakeRate, hkChannel.defaultDiagnosticWakeRate);
    }
    else {
      mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, hkChannel, 0, 0);
    }

    // APSS sensor power modeling
    mission.apssModel.paePoweredOn.set(paeOn);
    mission.apssModel.setComponentState(APSSModel.Component.TWINS_PY, twinsPyOn);
    mission.apssModel.setComponentState(APSSModel.Component.TWINS_MY, twinsMyOn);
    mission.apssModel.setComponentState(APSSModel.Component.P, psOn);
    mission.apssModel.setComponentState(APSSModel.Component.IFG, ifgOn);
    mission.apssModel.setComponentState(APSSModel.Component.APSS_BUS_V, _28vOn);

    // Power modeling
    List.of(
        Pair.of(paeOn, List.of(
            PelItem.APSS_IDLE_PAE,
            PelItem.APSS_GEN_EXT)),
        Pair.of(twinsPyOn, List.of(
            PelItem.APSS_TWINSPY_EXT,
            PelItem.APSS_TWINSPY_PAE,
            PelItem.APSS_HEATPY_EXT,
            PelItem.APSS_HEATPY_PAE
        )),
        Pair.of(twinsMyOn, List.of(
            PelItem.APSS_TWINSMY_EXT,
            PelItem.APSS_TWINSMY_PAE,
            PelItem.APSS_HEATMY_EXT,
            PelItem.APSS_HEATMY_PAE
        )),
        Pair.of(psOn, List.of(
            PelItem.APSS_PS_EXT,
            PelItem.APSS_PS_PAE
        )),
        Pair.of(ifgOn, List.of(
            PelItem.APSS_IFG_EXT,
            PelItem.APSS_IFG_PAE
        )),
        Pair.of(_28vOn, List.of(
            PelItem.APSS_28V_EXT,
            PelItem.APSS_28V_PAE
        ))
    ).forEach(p -> {
      final var state = p.getLeft() ? "on" : "off";
      p.getRight().forEach(i -> mission.powerModel.setPelState(i, state));
    });

//    mmpatSetParamData("twinshtrpy_tsetpoint", "st_value_list", to_string(TWINS_HTR_PY_setpoint));
//    mmpatSetParamData("twinshtrmy_tsetpoint", "st_value_list", to_string(TWINS_HTR_MY_setpoint));

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
