package gov.nasa.jpl.aerielander.models.wake;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

// TODO this is a temporary solution, resources.aaf lists the WAKE resource as being under the SEIS
//   subsystem but I'm not sure if that's really correct? So for now this is a standalone model.
public class WakeModel {

  public enum WakeType {
    NONE,
    FULL,
    DIAGNOSTIC
  }

  public final Register<WakeType> wakeType = Register.forImmutable(WakeType.NONE);

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/wakeType", basePath), wakeType, new EnumValueMapper<>(WakeType.class));
  }
}
