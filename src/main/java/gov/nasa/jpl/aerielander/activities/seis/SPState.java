package gov.nasa.jpl.aerielander.activities.seis;

public record SPState(boolean sp1on, boolean sp2on, boolean sp3on) {
  public static SPState allOn() { return new SPState(true, true, true); }
  public static SPState allOff() { return new SPState(false, false, false); }
}
