package gov.nasa.jpl.aerielander.models.data;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerielander.models.data.DataConfig.APID;

public final class InstrumentHKChannel {
  public final APID apid;
  public final double defaultFullWakeRate, defaultDiagnosticWakeRate;
  public final Register<Double> fullWakeRate, diagnosticWakeRate;

  public InstrumentHKChannel(final APID apid, final double defaultFullWakeRate, final double defaultDiagnosticWakeRate) {
    this.apid = apid;
    this.defaultFullWakeRate = defaultFullWakeRate;
    this.defaultDiagnosticWakeRate = defaultDiagnosticWakeRate;
    this.fullWakeRate = Register.forImmutable(defaultFullWakeRate);
    this.diagnosticWakeRate = Register.forImmutable(defaultDiagnosticWakeRate);
  }
}
