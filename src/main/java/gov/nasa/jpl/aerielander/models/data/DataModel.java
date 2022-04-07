package gov.nasa.jpl.aerielander.models.data;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Condition;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.types.RealDynamics;
import gov.nasa.jpl.aerielander.models.data.DataConfig.APID;
import gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName;
import gov.nasa.jpl.aerielander.models.wake.WakeModel.WakeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.spawn;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.DART;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.FPT;

public final class DataModel {

  private final Map<ChannelName, VirtualChannel> virtualChannelMap;
  private final Map<APID, APIDModel> apidModelMap;

  private final Register<FPT> activeFPT;
  public final Register<FPT> defaultFPT;
  public final Register<DART> defaultDART;
  public final HKModel hkModel = new HKModel();

  public DataModel() {
    virtualChannelMap = Map.ofEntries(
        Map.entry(ChannelName.RETX, new VirtualChannel(1000, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC00, new VirtualChannel(240, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC01, new VirtualChannel(50, ChannelName.VC06)),
        Map.entry(ChannelName.VC02, new VirtualChannel(100, ChannelName.VC05)),
        Map.entry(ChannelName.VC03, new VirtualChannel(50, ChannelName.VC06)),
        Map.entry(ChannelName.VC04, new VirtualChannel(50, ChannelName.VC06)),
        Map.entry(ChannelName.VC05, new VirtualChannel(300, ChannelName.VC06)),
        Map.entry(ChannelName.VC06, new VirtualChannel(50, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC07, new VirtualChannel(200, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC08, new VirtualChannel(100, ChannelName.VC05)),
        Map.entry(ChannelName.VC09, new VirtualChannel(600, ChannelName.VC06)),
        Map.entry(ChannelName.VC10, new VirtualChannel(0.03, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC11, new VirtualChannel(0.03, ChannelName.DISCARD)),
        Map.entry(ChannelName.VC12, new VirtualChannel(0.03, ChannelName.DISCARD))
        );

    apidModelMap = new HashMap<>();
    for (final var apid : APID.values()) apidModelMap.put(apid, new APIDModel());

    activeFPT = Register.forImmutable(FPT.DEFAULT);
    defaultFPT = Register.forImmutable(FPT.DEFAULT);
    defaultDART = Register.forImmutable(DART.DEFAULT);
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    for (final var entry : virtualChannelMap.entrySet()) {
      final var name = entry.getKey();
      final var vc = entry.getValue();
      registrar.real("%s/%s/rate".formatted(basePath, name), vc.volume.rate);
      registrar.real("%s/%s/volume".formatted(basePath, name), vc.volume);
      if (vc.overflowChannelId != DataConfig.ChannelName.DISCARD) {
        registrar.real("%s/%s/overflow/volume".formatted(basePath, name), vc.overflow);
        registrar.real("%s/%s/overflow/rate".formatted(basePath, name), vc.overflow.rate);
        registrar.real("%s/%s/discarded/volume".formatted(basePath, name), () -> RealDynamics.constant(0));
        registrar.real("%s/%s/discarded/rate".formatted(basePath, name), () -> RealDynamics.constant(0));
      } else {
        registrar.real("%s/%s/overflow/volume".formatted(basePath, name), () -> RealDynamics.constant(0));
        registrar.real("%s/%s/overflow/rate".formatted(basePath, name), () -> RealDynamics.constant(0));
        registrar.real("%s/%s/discarded/volume".formatted(basePath, name), vc.overflow);
        registrar.real("%s/%s/discarded/rate".formatted(basePath, name), vc.overflow.rate);
      }
    }

    for (final var entry : apidModelMap.entrySet()) {
      String path = "%s/apids/%s".formatted(basePath, entry.getKey());
      entry.getValue().registerResources(registrar, path);
    }

    registrar.discrete("%s/defaultFPT", defaultFPT, new EnumValueMapper<>(FPT.class));
    registrar.discrete("%s/defaultDART", defaultDART, new EnumValueMapper<>(DART.class));
    registrar.discrete("%s/activeFPT", activeFPT, new EnumValueMapper<>(FPT.class));

    hkModel.registerResources(registrar, "%s/hk".formatted(basePath));
  }

  public Optional<APIDModel> getApidModel(final APID apid) {
    return Optional.ofNullable(apidModelMap.get(apid));
  }

  public void updateDART(final DART dart) {
    for (final var entry : dart.map.entrySet()) {
      final var apid = entry.getKey();
      final var vc = entry.getValue();
      apidModelMap.get(apid).updateRoute(vc);
    }
  }

  public void updateFPT(final FPT fpt) {
    this.activeFPT.set(fpt);
  }

  public void setDefaultDART() {
    updateDART(defaultDART.get());
  }

  public void setDefaultFPT() {
    updateFPT(defaultFPT.get());
  }

  public void increaseDataRate(final APID apid, final double dRate) {
    apidModelMap.get(apid).increaseDataRate(dRate);
  }

  public Iterable<Map.Entry<ChannelName, VirtualChannel>> getChannels() {
    return virtualChannelMap.entrySet();
  }

  public VirtualChannel getChannel(final ChannelName channelName) {
    return virtualChannelMap.get(channelName);
  }

  /**
   * @param channels List of VC names to include in the condition
   * @param threshold Min value a channel must have for the condition to become true;
   * @return Condition that will be satisfied when any of the provided channels exceed the provided threshold
   */
  public Condition whenAnyVCExceedsThreshold(final List<ChannelName> channels, final double threshold) {
    var firstVC = virtualChannelMap.get(channels.get(0));
    var condition = firstVC.volume.isBetween(threshold, firstVC.limit);
    for (final var channel : channels.subList(1, channels.size())) {
      final var vc = virtualChannelMap.get(channel);
      condition = condition.or(vc.volume.isBetween(threshold, vc.limit));
    }
    return condition;
  }

  public FPT getCurrentFPT() {
    return this.activeFPT.get();
  }

  public void setInstrumentHKRate(final WakeType wakeType, final InstrumentHKChannel hkChannel, final double fullRate, final double diagnosticRate) {
    final var deltaRate = switch (wakeType) {
      case FULL -> fullRate - hkChannel.fullWakeRate.get();
      case DIAGNOSTIC -> diagnosticRate - hkChannel.diagnosticWakeRate.get();
      case NONE -> 0;
    };

    increaseDataRate(hkChannel.apid, deltaRate);

    hkChannel.fullWakeRate.set(fullRate);
    hkChannel.diagnosticWakeRate.set(diagnosticRate);
  }

  public void enableAllInstrumentHKRates(final WakeType wakeType) {
    switch (wakeType) {
      case FULL -> enableAllInstrumentFullHKRates();
      case DIAGNOSTIC -> enableAllInstrumentDiagnosticHKRates();
    }
  }

  public void enableAllInstrumentFullHKRates() {
    for (final var hkChannel : hkModel.allChannels) {
      increaseDataRate(hkChannel.apid, hkChannel.fullWakeRate.get());
    }
  }

  public void enableAllInstrumentDiagnosticHKRates() {
    for (final var hkChannel : hkModel.allChannels) {
      increaseDataRate(hkChannel.apid, hkChannel.diagnosticWakeRate.get());
    }
  }

  public void disableAllInstrumentHKRates(final WakeType wakeType) {
    switch (wakeType) {
      case FULL -> disableAllInstrumentFullHKRates();
      case DIAGNOSTIC -> disableAllInstrumentDiagnosticHKRates();
    }
  }

  public void disableAllInstrumentFullHKRates() {
    for (final var hkChannel : hkModel.allChannels) {
      increaseDataRate(hkChannel.apid, -hkChannel.fullWakeRate.get());
    }
  }

  public void disableAllInstrumentDiagnosticHKRates() {
    for (final var hkChannel : hkModel.allChannels) {
      increaseDataRate(hkChannel.apid, -hkChannel.diagnosticWakeRate.get());
    }
  }

  public final class VirtualChannel {
    // Encapsulate these so clients to the model can't muck up the rates
    public final Accumulator volume, overflow;
    public final double limit;
    public final ChannelName overflowChannelId;

    private VirtualChannel(final double limit, final ChannelName overflowChannelId) {
      this.volume = new Accumulator(0, 0);
      this.overflow = new Accumulator(0, 0);
      this.limit = limit;
      this.overflowChannelId = overflowChannelId;
      spawn(this::monitorOverflow);
    }

    public void increaseDataRate(final double dRate) {
      if (this.volume.get() < this.limit) {
        this.volume.rate.add(dRate);
      } else {
        if (this.overflow.rate.get() + dRate < 0.0) {
          this.deactivateOverflow();
          this.volume.rate.add(dRate);
        } else {
          this.increaseOverflowRate(dRate);
        }
      }
    }

    private void monitorOverflow() {
      while (true) {
        waitUntil(this.volume.isBetween(this.limit, this.limit));
        this.activateOverflow();
        waitUntil(this.volume.isBetween(0, Math.nextDown(this.limit)));
        this.deactivateOverflow();
      }
    }

    private void activateOverflow() {
      final var spillRate = this.volume.rate.get();
      this.volume.rate.add(-spillRate);
      this.increaseOverflowRate(spillRate);
    }

    private void deactivateOverflow() {
      final var spillRate = this.overflow.rate.get();
      this.volume.rate.add(spillRate);
      this.increaseOverflowRate(-spillRate);
    }

    private void increaseOverflowRate(final double dOverflowRate) {
      this.overflow.rate.add(dOverflowRate);
      if (this.overflowChannelId != ChannelName.DISCARD) {
        virtualChannelMap.get(this.overflowChannelId).increaseDataRate(dOverflowRate);
      }
    }
  }

  public final class APIDModel {
    private Register<ChannelName> routedVC;
    private Register<Double> dataRate;

    public APIDModel() {
      this.routedVC = Register.forImmutable(ChannelName.VC00);
      this.dataRate = Register.forImmutable(0.0);
    }

    public void registerResources(final Registrar registrar, final String basePath) {
      registrar.discrete("%s/routedVC".formatted(basePath), routedVC, new EnumValueMapper<>(ChannelName.class));
      registrar.discrete("%s/dataRate".formatted(basePath), dataRate, new DoubleValueMapper());
    }

    public VirtualChannel getRoutedVirtualChannel() { return virtualChannelMap.get(routedVC.get()); }

    public void increaseDataRate(final double dRate) {
      final var current = this.dataRate.get();
      this.dataRate.set(current + dRate);
      virtualChannelMap.get(this.routedVC.get()).increaseDataRate(dRate);
    }

    public void updateRoute(final ChannelName channelName) {
      virtualChannelMap.get(this.routedVC.get()).increaseDataRate(-this.dataRate.get());
      this.routedVC.set(channelName);
      virtualChannelMap.get(this.routedVC.get()).increaseDataRate(this.dataRate.get());
    }
  }
}
