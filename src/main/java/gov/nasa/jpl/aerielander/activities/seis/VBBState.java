package gov.nasa.jpl.aerielander.activities.seis;

public record VBBState(boolean vbb1on, boolean vbb2on, boolean vbb3on) {
  public static VBBState allOn() { return new VBBState(true, true, true); }
  public static VBBState allOff() { return new VBBState(false, false, false); }
}
