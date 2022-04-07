package gov.nasa.jpl.aerielander.models.apss;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

public final class APSSModel {

  public static final Double LIMIT_RESOLUTION = 0.0001;

  public enum Component {
    TWINS_PY,
    TWINS_MY,
    P,
    IFG,
    APSS_BUS_V
  }

  public static final record ComponentModel(
      Register<Boolean> state,
      Register<ComponentRate> inRate,
      Register<ComponentRate> outRate)
  {
    public ComponentModel(final boolean state, final ComponentRate inRate, final ComponentRate outRate) {
      this(Register.forImmutable(state), Register.forImmutable(inRate), Register.forImmutable(outRate));
    }
  }

  public static final record ComponentRate(double defaultRate, double bothBoomsOnRate) { }

  public final Register<Boolean> paePoweredOn = Register.forImmutable(false);

  private final Map<Component, ComponentModel> components = Collections.unmodifiableMap(Arrays.stream(Component.values())
      .collect(Collectors.toMap(v -> v, v -> new ComponentModel(false, new ComponentRate(0, 0), new ComponentRate(0, 0)))));

  private final Accumulator internalVolume = new Accumulator(0.0, 0.0);
  private final Accumulator volumeToSendToVC = new Accumulator(0.0, 0.0);
  private final Register<Double> continuousDataSentIn = Register.forImmutable(0.0);

  // Data rate for how fast APSS can process data and pass to lander in current configuration
  public final Register<Double> transferRate = Register.forImmutable(751.68/3600.0); // Mbits/sec

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/power_on/PAE", basePath), paePoweredOn, new BooleanValueMapper());
    components.forEach((k, v) -> registrar.discrete(String.format("%s/power_on/%s", basePath, k.name()), v.state, new BooleanValueMapper()));
    registrar.real(String.format("%s/internal_volume/volume", basePath), internalVolume);
    registrar.real(String.format("%s/internal_volume/rate", basePath), internalVolume.rate);
    registrar.real(String.format("%s/volume_to_send_to_vc/volume", basePath), volumeToSendToVC);
    registrar.real(String.format("%s/volume_to_send_to_vc/rate", basePath), volumeToSendToVC.rate);
    registrar.discrete(String.format("%s/continuous_data_sent_in", basePath), continuousDataSentIn, new DoubleValueMapper());
    registrar.discrete(String.format("%s/transfer_rate", basePath), transferRate, new DoubleValueMapper());
  }

  public Register<Boolean> getComponentState(final Component component) { return components.get(component).state; }

  public void setComponentState(final Component component, final boolean state) {
    components.get(component).state.set(state);
    updateComponentRates();
  }

  public double getInternalVolume() { return internalVolume.get(); }
  public double getVolumeToSendToVC() { return volumeToSendToVC.get(); }

  private void updateComponentRates() {
    // Zero out rates to reset further down
    internalVolume.rate.add(-internalVolume.rate.get());
    volumeToSendToVC.rate.add(-volumeToSendToVC.rate.get());

    // If two booms are on use the `bothBoomsOnRate`, otherwise use the default rate
    final var bothBoomsOn = components.get(Component.TWINS_PY).state.get() && components.get(Component.TWINS_MY).state.get();

    for (final var c : Component.values()) {
      final var inRate = bothBoomsOn ? components.get(c).inRate.get().bothBoomsOnRate : components.get(c).inRate.get().defaultRate;
      internalVolume.rate.add(inRate);
      final var outRate = bothBoomsOn ? components.get(c).outRate.get().bothBoomsOnRate : components.get(c).outRate.get().defaultRate;
      volumeToSendToVC.rate.add(outRate);
    }
  }

  public void setComponentInRates(final Component component, final double defaultRate, final double bothBoomsOnRate) {
    components.get(component).inRate.set(new ComponentRate(defaultRate, bothBoomsOnRate));
  }

  public void setComponentOutRates(final Component component, final double defaultRate, final double bothBoomsOnRate) {
    components.get(component).outRate.set(new ComponentRate(defaultRate, bothBoomsOnRate));
  }

  public void dumpInternalData(final Duration duration, final double internalVolumeToDump, final double vcVolumeToDump) {
    final var internalRate = internalVolumeToDump / duration.ratioOver(Duration.SECONDS);
    final var sendToVCRate = vcVolumeToDump / duration.ratioOver(Duration.SECONDS);

    internalVolume.rate.add(-internalRate);
    volumeToSendToVC.rate.add(-sendToVCRate);
    delay(duration);
    internalVolume.rate.add(internalRate);
    volumeToSendToVC.rate.add(sendToVCRate);
  }

  public void dumpInternalData(final Duration duration) {
    dumpInternalData(duration, internalVolume.get(), volumeToSendToVC.get());
  }

  public void addContinuousDataSentIn(final double volume) {
    continuousDataSentIn.set(continuousDataSentIn.get() + volume);
  }
}
