package gov.nasa.jpl.aerielander.config;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;

public record SchedulingParams (
    double SEIS_SCI_RUNOUT_FRAC,
    double APSS_SCI_RUNOUT_FRAC,
    double HeatProbe_SCI_RUNOUT_FRAC,
    Duration MAX_RUNOUT_SEIS_SCI_PROC_DUR,
    Duration MIN_RUNOUT_SEIS_SCI_PROC_DUR,
    Duration MAX_RUNOUT_APSS_SCI_PROC_DUR,
    Duration MIN_RUNOUT_APSS_SCI_PROC_DUR,
    Duration MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR,
    Duration MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR,
    Duration RUNOUT_CLEANUP_MARGIN,
    Duration MIN_SUB_RUNOUT_DUR,
    Duration MinSleepDuration,
    Duration MaxSleepDuration,
    boolean PlacePostUplinkWake
) {
  public static final SchedulingParams defaults =
      new SchedulingParams(
          0.5,
          0.5,
          0.9,
          Duration.of(20, MINUTES),
          Duration.of(5, MINUTES),
          Duration.of(20, MINUTES),
          Duration.of(5, MINUTES),
          Duration.of(60, MINUTES),
          Duration.of(5, MINUTES),
          Duration.of(5, MINUTES),
          Duration.of(15, MINUTES),
          Duration.of(20, MINUTES),
          Duration.of(170, MINUTES),
          true
      );
}
