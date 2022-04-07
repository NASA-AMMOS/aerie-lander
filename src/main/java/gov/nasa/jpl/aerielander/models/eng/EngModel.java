package gov.nasa.jpl.aerielander.models.eng;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import java.util.HashMap;
import java.util.Map;

public final class EngModel {

  public enum Component {
    Lander,
    APSS,
    SEIS,
    HeatProbe,
    IDS
  }

  public final Map<Component, Register<Boolean>> safeModeMap;

  public EngModel() {
    safeModeMap = new HashMap<>();
    for (final var component : Component.values()) {
      safeModeMap.put(component, Register.forImmutable(false));
    }
  }

  public void setSafeMode(final Component mode, final boolean on) {
    safeModeMap.get(mode).set(on);
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    safeModeMap.keySet().forEach(mode ->
                                     registrar.discrete(String.format("%s/safe_mode/%s", basePath, mode.name()), safeModeMap.get(mode), new BooleanValueMapper()));

  }
}
