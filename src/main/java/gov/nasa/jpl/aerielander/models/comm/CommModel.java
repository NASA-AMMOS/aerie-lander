package gov.nasa.jpl.aerielander.models.comm;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CommModel {
  public enum Orbiter {
    ODY,
    MRO,
    TGO,
    MVN,
    MEX
  }

  public enum XBandAntenna {
    EAST_MGA,
    WEST_MGA
  }

  private final Accumulator dataSent;
  private final Register<XBandAntenna> activeXBandAntenna;
  private final Map<Orbiter, Register<Boolean>> alternateUhfBlockInUseMap;

  public CommModel() {
    dataSent = new Accumulator(0, 0);
    activeXBandAntenna = Register.forImmutable(XBandAntenna.EAST_MGA);
    alternateUhfBlockInUseMap = new HashMap<>();
    for (final var orbiter : CommModel.Orbiter.values()) alternateUhfBlockInUseMap.put(orbiter, Register.forImmutable(false));
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    alternateUhfBlockInUseMap.keySet().forEach(mode ->
      registrar.discrete(String.format("%s/ufh/alternate_block_in_use/%s", basePath, mode.name()), alternateUhfBlockInUseMap.get(mode), new BooleanValueMapper()));
    registrar.topic("/mytopic", this.activeXBandAntenna.ref, new EnumValueMapper<>(XBandAntenna.class));
  }

  public XBandAntenna getXBandAntenna() {
    return this.activeXBandAntenna.get();
  }

  public void setXBandAntenna(final XBandAntenna antenna) {
    this.activeXBandAntenna.set(antenna);
  }

  public void increaseDownlinkRate(final double downlinkRate) {
    dataSent.rate.add(downlinkRate);
  }

  public void setAlternateUhfBlockInUse(final CommModel.Orbiter orbiter, final boolean enableAlternateBlock) {
    alternateUhfBlockInUseMap.get(orbiter).set(enableAlternateBlock);
  }

  public Map<CommModel.Orbiter, Register<Boolean>> getAlternateUhfBlockInUseMap() {
    return Collections.unmodifiableMap(alternateUhfBlockInUseMap);
  }
}
