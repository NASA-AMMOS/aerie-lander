package gov.nasa.jpl.aerielander.config;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

public record MasterActivityDurations(
    Duration GV_WAKEUP_TIME_OFFSET,
    Duration BOOT_INIT_DURATION,
    Duration LOAD_BLOCK_LIB_DURATION,
    Duration WAKEUP_FULL_DURATION,
    Duration WAKEUP_DIAG_DURATION,
    Duration FILE_MGMT_DURATION,
    Duration LME_CURVE_SEL_DURATION,
    Duration SUBMASTER_DIAG_DURATION,
    Duration FSW_DIAG_DURATION,
    Duration FILE_COPY_DURATION,
    Duration SHUTDOWN_FULL_DURATION,
    Duration SHUTDOWN_DIAG_DURATION
) {
  public static final MasterActivityDurations defaults =
      new MasterActivityDurations(
          Duration.of(35, SECONDS),
          Duration.of(2, MINUTES),
          Duration.of(43, SECONDS),
          Duration.of(7, MINUTES).plus(Duration.of(30, SECONDS)),
          Duration.of(4, MINUTES).plus(Duration.of(10, SECONDS)),
          Duration.of(4, SECONDS),
          Duration.of(35, SECONDS),
          Duration.of(1, MINUTES),
          Duration.of(1, MINUTES),
          Duration.of(1, SECONDS),
          Duration.of(8, MINUTES),
          Duration.of(5, MINUTES)
      );
}
