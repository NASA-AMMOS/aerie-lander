package gov.nasa.jpl.aerielander.models.seis;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.StringValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.mappers.seis.ChannelRateValueMapper;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.Channel;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.ChannelOutRateGroup;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.ChannelRate;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.Device;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.DeviceType;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.DeviceTypeMetrics;

public final class SeisModel {

  public final Register<Boolean> poweredOn = Register.forImmutable(false);
  public final Register<Boolean> mdeShouldBeOn = Register.forImmutable(false);

  private final Map<Device, Register<Boolean>> deviceOn = Collections.unmodifiableMap(Arrays.stream(Device.values())
      .collect(Collectors.toMap(v -> v, v -> Register.forImmutable(false))));

  private final Map<Channel, Register<ChannelRate>> channelRates = Collections.unmodifiableMap(Arrays.stream(Channel.values())
      .collect(Collectors.toMap(v -> v, v -> Register.forImmutable(new ChannelRate(0.0, 0.0)))));

  private final Map<DeviceType, DeviceTypeMetrics> deviceTypeMetrics = Collections.unmodifiableMap(Arrays.stream(DeviceType.values())
      .collect(Collectors.toMap(v -> v, v -> new DeviceTypeMetrics())));

  // Register encapsulates entire list since the list must be mutable
  private final Register<List<ChannelOutRateGroup>> combinedChannelOutRates = Register.forImmutable(new ArrayList<>());

  private final Accumulator internalVolume = new Accumulator(0.0, 0.0);
  private final Accumulator volumeToSendToVC = new Accumulator(0.0, 0.0);
  private final Register<Double> continuousDataSentIn = Register.forImmutable(0.0);

  // Data rate for how fast SEIS can process data and pass to lander in current configuration
  public final Register<Double> transferRate = Register.forImmutable(1666.66/3600.0); // Mbits/sec

  public final Register<SeisConfig.VBBMode> vbbMode = Register.forImmutable(SeisConfig.VBBMode.SCI);

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/power_on", basePath), poweredOn, new BooleanValueMapper());
    registrar.discrete(String.format("%s/mde_should_be_on", basePath), mdeShouldBeOn, new BooleanValueMapper());
    registrar.real(String.format("%s/internal_volume/volume", basePath), internalVolume);
    registrar.real(String.format("%s/internal_volume/rate", basePath), internalVolume.rate);
    registrar.real(String.format("%s/volume_to_send_to_vc/volume", basePath), volumeToSendToVC);
    registrar.real(String.format("%s/volume_to_send_to_vc/rate", basePath), volumeToSendToVC.rate);
    registrar.discrete(String.format("%s/continuous_data_sent_in", basePath), continuousDataSentIn, new DoubleValueMapper());
    registrar.discrete(String.format("%s/transfer_rate", basePath), transferRate, new DoubleValueMapper());
    registrar.discrete(String.format("%s/vbb_mode", basePath), () -> vbbMode.get().name(), new StringValueMapper());
    channelRates.forEach((channel, rate) ->
        registrar.discrete(String.format("%s/channel/%s/", basePath, channel.name()), rate, new ChannelRateValueMapper()));
    deviceTypeMetrics.forEach((deviceType, metrics) -> {
        registrar.discrete(String.format("%s/device_type/%s/sampling_rate", basePath, deviceType.name()), metrics.samplingRate(), new DoubleValueMapper());
        registrar.discrete(String.format("%s/device_type/%s/gain", basePath, deviceType.name()), () -> metrics.gain().get().name(), new StringValueMapper());
    });
  }

  public double getInternalVolume() { return internalVolume.get(); }
  public double getVolumeToSendToVC() { return volumeToSendToVC.get(); }

  public void setDeviceState(final Device device, final boolean state) {
    deviceOn.get(device).set(state);
    updateChannelRates();
  }

  public void setChannelRates(final Map<Channel, ChannelRate> newChannelRates) {
    newChannelRates.forEach((channel, rate) -> channelRates.get(channel).set(rate));
    updateChannelRates();
  }

  public void setCombinedChannelOutRates(final List<ChannelOutRateGroup> newCombinedChannelOutRates) {
    combinedChannelOutRates.set(newCombinedChannelOutRates);
    updateChannelRates();
  }

  public void setSamplingRate(final DeviceType deviceType, final double samplingRate) {
    deviceTypeMetrics.get(deviceType).samplingRate().set(samplingRate);
    updateChannelRates();
  }

  public void setGain(final DeviceType deviceType, final SeisConfig.Gain gain) {
    deviceTypeMetrics.get(deviceType).gain().set(gain);
    updateChannelRates();
  }

  private void updateChannelRates() {
    // Zero out rates to reset further down
    internalVolume.rate.add(-internalVolume.rate.get());
    volumeToSendToVC.rate.add(-volumeToSendToVC.rate.get());

    // INTERNAL VOLUME

    channelRates.forEach((channel, rate) -> internalVolume.rate.add(rate.get().inRate()));

    // VOLUME TO SEND TO VC

    final var channelsOn = new HashSet<Channel>(); // Keep track of all channels that are on

    // VBB
    List.of(Device.VBB1, Device.VBB2, Device.VBB3).stream()
        .filter(d -> deviceOn.get(d).get())
        .forEach(device ->
        {
          final var velMetrics = deviceTypeMetrics.get(DeviceType.VEL);
          final var velRate = velMetrics.samplingRate().get();
          if (velRate != 0) {
            final var freq = velRate == 20.0 ? "LR" : "HR";
            final var channel = Channel.valueOf(
                String.join("_", device.name(), "VEL", freq, velMetrics.gain().get().abbrev, vbbMode.get().abbrev));
            channelsOn.add(channel);
          }

          final var posMetrics = deviceTypeMetrics.get(DeviceType.POS);
          final var posRate = posMetrics.samplingRate().get();
          if (posRate != 0) {
            final var freq = posRate == 0.1 ? "LR" : "HR";
            final var channel = Channel.valueOf(
                String.join("_", device.name(), "POS", freq, posMetrics.gain().get().abbrev, vbbMode.get().abbrev));
            channelsOn.add(channel);
          }

          final var tempRate = deviceTypeMetrics.get(DeviceType.TEMP).samplingRate().get();
          if (tempRate != 0) {
            final var freq = tempRate == 0.1 ? "LR" : "HR";
            final var channel = Channel.valueOf(String.join("_", device.name(), "TMP", freq));
            channelsOn.add(channel);
          }
        });

    // SP
    List.of(Device.SP1, Device.SP2, Device.SP3).stream()
        .filter(d -> deviceOn.get(d).get())
        .forEach(device ->
        {
          final var metrics = deviceTypeMetrics.get(DeviceType.SP);
          final var spRate = metrics.samplingRate().get();
          if (spRate != 0) {
            final var freq = spRate == 20.0 ? "LR" : "HR";
            final var channel = Channel.valueOf(String.join("_", device.name(), freq, metrics.gain().get().abbrev));
            channelsOn.add(channel);
          }
        });

    // SCIT
    {
      final var scitRate = deviceTypeMetrics.get(DeviceType.SCIT).samplingRate().get();
      final var channel = scitRate == 0.1 ? Channel.SCIT_LR : Channel.SCIT_HR;
      channelsOn.add(channel);
    }

    // Sum all channel rates
    channelsOn.forEach(channel -> volumeToSendToVC.rate.add(channelRates.get(channel).get().outRate()));

    // Add combined channel out rates if all channels within a group are on
    combinedChannelOutRates.get().stream()
        .filter(group -> channelsOn.containsAll(group.channels()))
        .forEach(group -> volumeToSendToVC.rate.add(group.outRate()));
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

  public void addContinuousDataSentIn(final double volume) {
    continuousDataSentIn.set(continuousDataSentIn.get() + volume);
  }

  public void runMDEStateMachine(final PowerModel powerModel) {
    var state = "off";

    if (poweredOn.get() && mdeShouldBeOn.get()){
      // "on" uses the most power, followed by "startup" then "htr", so we check for them in that order
      state = "on";
    }
    // TODO this requires constraints
//    else if (poweredOn.get() && SEIS_GET_TILT_CONSTRAINT_TRACKER.get() > 0) {
//      // only in 'startup' for long periods of time during tilt measurements
//      state = "startup";
//    }
    else if (poweredOn.get() && powerModel.getPelState(PelItem.SEIS_MDEHTR_EBOX).equals("on")) {
      // if heater is on and higher-power MDE-modes are not active, MDE is in heater mode
      state = "htr";
    }

    powerModel.setPelState(PelItem.SEIS_MDE_EXT, state);
    powerModel.setPelState(PelItem.SEIS_MDE_EBOX, state);
  }
}
