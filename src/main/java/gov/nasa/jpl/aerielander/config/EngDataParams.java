package gov.nasa.jpl.aerielander.config;

public record EngDataParams(
    double AWAKE_ENG_DATA_RATE,
    double UHF_PREP_DATA_DUMP_DIAG,
    double UHF_PREP_DATA_DUMP_NO_DIAG,
    double UHF_ACTIVE_DATA_RATE,
    double SHUTDOWN_DATA_DUMP,
    double WAKEUP_FULL_DATA_DUMP,
    double WAKEUP_DIAG_DATA_DUMP
) {
  public static final EngDataParams defaults =
      new EngDataParams(
          1.0104/3600,
          1.532,
          0.971,
          0.432/3600,
          0.000,
          0.526,
          0.256);
}
