package gov.nasa.jpl.aerielander.models.heatprobe;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DurationValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import java.util.Map;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

public final class HeatProbeModel {
  public enum PowerState {
    On,
    Off
  }

  public enum SSAState {
    Off,
    Idle,
    Checkout,
    Single,
    Monitoring
  }

  public enum RADState {
    Off,
    Idle,
    Single,
    Calibration,
    Standard,
    Hourly
  }

  public enum HeatProbeParameter {
    PARAM_HeatProbe_MON_TEMP_DURATION,
    PARAM_HeatProbe_MON_WAIT_DURATION,
    PARAM_HeatProbe_SINGLEPEN_COOL_DURATION,
    PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION,
    PARAM_HeatProbe_HAMMER_TIMEOUT,
    PARAM_HeatProbe_CO_TEMP_DURATION,
    PARAM_HeatProbe_CO_TEMA_DURATION,
    PARAM_HeatProbe_CO_STATIL_TLM_DURATION,
    PARAM_RAD_HEATUP_DURATION,
    PARAM_RAD_MEAS_DURATION,
    PARAM_RAD_HOURLY_WAIT_DURATION,
    PARAM_RAD_STD_WAIT_DURATION_SHORT,
    PARAM_RAD_STD_WAIT_DURATION_LONG,
    PARAM_RAD_SINGLEMEAS_DURATION,
    PARAM_RAD_CAL_MEAS_DURATION
  }

  private final Register<PowerState> powerState;
  private final Accumulator HeatProbeInternalData;
  private final Register<Double> sciDataSentInActivity;
  private final Register<SSAState> ssaState;
  private final Register<RADState> radState;
  private final Map<HeatProbeParameter, Register<Duration>> parametersInTable;
  private final Map<HeatProbeParameter, Register<Duration>> parametersCurrent;

  public HeatProbeModel() {
    powerState = Register.forImmutable(PowerState.Off);
    HeatProbeInternalData = new Accumulator(0, 0);
    sciDataSentInActivity = Register.forImmutable(0.0);
    ssaState = Register.forImmutable(SSAState.Off);
    radState = Register.forImmutable(RADState.Off);
    parametersInTable = Map.ofEntries(
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_HEATUP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_HEATUP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_MEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_MEAS_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT))),
        Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG))),
        Map.entry(HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION)))
    );
    parametersCurrent = Map.ofEntries(
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_HEATUP_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_HEATUP_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_MEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_MEAS_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT))),
        Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG))),
        Map.entry(HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION))),
        Map.entry(HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION, Register.forImmutable(defaultParameters.get(HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION)))
    );
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/powerState", basePath), powerState, new EnumValueMapper<>(PowerState.class));
    registrar.real(String.format("%s/internalData", basePath), HeatProbeInternalData);
    registrar.discrete(String.format("%s/sciDataSentInActivity", basePath), sciDataSentInActivity, new DoubleValueMapper());
    registrar.discrete(String.format("%s/ssaState", basePath), ssaState, new EnumValueMapper<>(SSAState.class));
    registrar.discrete(String.format("%s/radState", basePath), radState, new EnumValueMapper<>(RADState.class));
    for (final var entry : parametersInTable.entrySet()) {
      registrar.discrete(String.format("%s/tableParams/%s", basePath, entry.getKey().toString()), entry.getValue(), new DurationValueMapper());
    }
    for (final var entry :parametersCurrent.entrySet()) {
      registrar.discrete(String.format("%s/currentParams/%s", basePath, entry.getKey().toString()), entry.getValue(), new DurationValueMapper());
    }
  }

  public void setSSAState(final SSAState state) {
    ssaState.set(state);
  }

  public SSAState getSSAState() {
    return ssaState.get();
  }

  public void setRADState(final RADState state) {
    radState.set(state);
  }

  public RADState getRADState() {
    return radState.get();
  }

  public void powerOn() {
    powerState.set(PowerState.On);
  }

  public void powerOff() {
    powerState.set(PowerState.Off);
  }

  public void setDataRate(final double rate) {
    HeatProbeInternalData.rate.add(rate - HeatProbeInternalData.rate.get());
  }

  public void increaseDataRate(final double rate) {
    HeatProbeInternalData.rate.add(rate);
  }

  public double getDataVolume() {
    return HeatProbeInternalData.get();
  }

  public void setSciDataSentInActivity(final double amount) {
    sciDataSentInActivity.set(sciDataSentInActivity.get() + amount);
  }

  public void updateTableParameter(final HeatProbeParameter parameter, final Duration value) {
    parametersInTable.get(parameter).set(value);
  }

  public void updateCurrentParameter(final HeatProbeParameter parameter, final Duration value) {
    parametersCurrent.get(parameter).set(value);
  }

  public Duration getCurrentParameter(final HeatProbeParameter parameter) {
    return parametersCurrent.get(parameter).get();
  }

  public void setParametersToTableValues() {
    for (final var entry : parametersInTable.entrySet()) {
      parametersCurrent.get(entry.getKey()).set(entry.getValue().get());
    }
  }

  private final Map<HeatProbeParameter, Duration> defaultParameters = Map.ofEntries(
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION, Duration.of(5, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION, Duration.of(55, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION, Duration.of(3, HOURS)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION, Duration.of(1, HOURS)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT, Duration.of(4, HOURS)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION, Duration.of(10, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION, Duration.of(12, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION, Duration.of(14, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_RAD_HEATUP_DURATION, Duration.of(15, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_RAD_MEAS_DURATION, Duration.of(20, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION, Duration.of(56, MINUTES).plus(Duration.of(4, SECONDS))),
      Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT, Duration.of(2, HOURS).plus(Duration.of(4, MINUTES)).plus(Duration.of(58, SECONDS))),
      Map.entry(HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG, Duration.of(8, HOURS).plus(Duration.of(14, MINUTES)).plus(Duration.of(52, SECONDS))),
      Map.entry(HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION, Duration.of(15, MINUTES)),
      Map.entry(HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION, Duration.of(5, MINUTES))
  );
}
