package gov.nasa.jpl.aerielander.config;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

public record CommParameters(
    Duration XBAND_PRE_COMM_MARGIN,
    Duration XBAND_POST_COMM_MARGIN,
    Duration UHF_PRE_COMM_MARGIN,
    Duration UHF_POST_COMM_MARGIN,
    Duration XBAND_PREP_OVERHEAD,
    Duration XBAND_CLEANUP_DURATION,
    Duration UHF_RT_DUR_1,
    Duration UHF_SCI_DUR_2_OFFSET,
    Duration UHF_RT_3_DUR,
    Duration UHF_CLEANUP_DUR,
    double UHF_RETURN_LINK_UNENCODED_EFFICIENCY,
    double UHF_RETURN_LINK_ENCODED_EFFICIENCY,
    double UHF_DATA_VOLUME_SCALAR,
    boolean UHF_DELAY_FOR_VC00
) {
  public static final CommParameters defaults =
      new CommParameters(
          Duration.of(5, MINUTES),
          Duration.of(5, MINUTES),
          Duration.of(35, MINUTES),
          Duration.of(8, MINUTES),
          Duration.of(23, SECONDS),
          Duration.of(19, SECONDS),
          Duration.of(35, SECONDS),
          Duration.ZERO,
          Duration.ZERO,
          Duration.of(40, SECONDS),
          0.9811,
          0.8507,
          0.91,
          false
      );
}
