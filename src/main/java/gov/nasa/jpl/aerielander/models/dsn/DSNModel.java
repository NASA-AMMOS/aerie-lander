package gov.nasa.jpl.aerielander.models.dsn;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.spawn;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;

public final class DSNModel {
  public enum DSNStation {
    Canberra,
    Madrid,
    Goldstone,
    None
  }

  public enum VisibilityEnum{
    InView,
    Hidden
  }

  public enum AllocatedEnum{
    Allocated,
    NotAllocated
  }

  private final Map<DSNStation,Register<AllocatedEnum>> allocated;
  private final Map<DSNStation, Register<VisibilityEnum>> visible;

  private final Register<DSNStation> currentStation;
  private final Map<DSNStation, StationMonitor> stations;

  private class StationMonitor {
    private final DSNStation station;

    public StationMonitor(DSNStation station){
      this.station = station;
      spawn(this::monitorStationStatus);
    }
    public void monitorStationStatus() {
      while(true) {
        waitUntil(visible
                      .get(this.station)
                      .is(VisibilityEnum.InView)
                      .and(allocated.get(station).is(AllocatedEnum.Allocated)));
        currentStation.set(this.station);
        waitUntil(visible
                      .get(station)
                      .is(VisibilityEnum.Hidden)
                      .or(allocated.get(station).is(AllocatedEnum.NotAllocated)));
        currentStation.set(DSNStation.None);
      }
    }
  }

  public DSNModel() {
    stations = new HashMap<>();
    allocated = new HashMap<>();
    visible = new HashMap<>();
    currentStation = Register.forImmutable(DSNStation.None);
    var withoutNone = Arrays.stream(DSNStation.values()).collect(Collectors.toList());
    withoutNone.remove(DSNStation.None);
    for (final var orbiter : withoutNone){
      allocated.put(orbiter, Register.forImmutable(AllocatedEnum.NotAllocated));
      visible.put(orbiter, Register.forImmutable(VisibilityEnum.Hidden));
    }
    for(var station:withoutNone){
      stations.put(station,new StationMonitor(station));
    }
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/allocstation", basePath), currentStation, new EnumValueMapper<>(DSNStation.class));
    allocated.keySet().forEach(station ->
      registrar.discrete(String.format("%s/allocated/%s", basePath, station.name()), allocated.get(station), new EnumValueMapper<>(AllocatedEnum.class)));
    visible.keySet().forEach(station ->
      registrar.discrete(String.format("%s/visible/%s", basePath, station.name()), visible.get(station), new EnumValueMapper<>(VisibilityEnum.class)));
  }

  public void setVisible(final DSNStation station, final VisibilityEnum isVisible) {
    visible.get(station).set(isVisible);
  }
  public void setAllocated(final DSNStation station, final AllocatedEnum isAllocated) {
    allocated.get(station).set(isAllocated);
  }


}
