package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DurationValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.CommParameters;

import java.util.Map;

public final class CommParametersMapper implements ValueMapper<CommParameters> {
  private final ValueMapper<Duration> durationValueMapper = new DurationValueMapper();
  private final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();
  private final ValueMapper<Boolean> booleanValueMapper = new BooleanValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.ofEntries(
        Map.entry("XBAND_PRE_COMM_MARGIN", ValueSchema.DURATION),
        Map.entry("XBAND_POST_COMM_MARGIN", ValueSchema.DURATION),
        Map.entry("UHF_PRE_COMM_MARGIN", ValueSchema.DURATION),
        Map.entry("UHF_POST_COMM_MARGIN", ValueSchema.DURATION),
        Map.entry("XBAND_PREP_OVERHEAD", ValueSchema.DURATION),
        Map.entry("XBAND_CLEANUP_DURATION", ValueSchema.DURATION),
        Map.entry("UHF_RT_DUR_1", ValueSchema.DURATION),
        Map.entry("UHF_SCI_DUR_2_OFFSET", ValueSchema.DURATION),
        Map.entry("UHF_RT_3_DUR", ValueSchema.DURATION),
        Map.entry("UHF_CLEANUP_DUR", ValueSchema.DURATION),
        Map.entry("UHF_RETURN_LINK_UNENCODED_EFFICIENCY", ValueSchema.REAL),
        Map.entry("UHF_RETURN_LINK_ENCODED_EFFICIENCY", ValueSchema.REAL),
        Map.entry("UHF_DATA_VOLUME_SCALAR", ValueSchema.REAL),
        Map.entry("UHF_DELAY_FOR_VC00", ValueSchema.BOOLEAN)
    ));
  }

  @Override
  public Result<CommParameters, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("XBAND_PRE_COMM_MARGIN")) return Result.failure("Expected field \"XBAND_PRE_COMM_MARGIN\", but not found: " + serializedValue);
    final var xband_pre_comm_margin$ = durationValueMapper.deserializeValue(map.get("XBAND_PRE_COMM_MARGIN"));
    if (xband_pre_comm_margin$.getKind() == Result.Kind.Failure) return Result.failure(xband_pre_comm_margin$.getFailureOrThrow());
    final var xband_pre_comm_margin = xband_pre_comm_margin$.getSuccessOrThrow();

    if (!map.containsKey("XBAND_POST_COMM_MARGIN")) return Result.failure("Expected field \"XBAND_POST_COMM_MARGIN\", but not found: " + serializedValue);
    final var xband_post_comm_margin$ = durationValueMapper.deserializeValue(map.get("XBAND_POST_COMM_MARGIN"));
    if (xband_post_comm_margin$.getKind() == Result.Kind.Failure) return Result.failure(xband_post_comm_margin$.getFailureOrThrow());
    final var xband_post_comm_margin = xband_post_comm_margin$.getSuccessOrThrow();

    if (!map.containsKey("UHF_PRE_COMM_MARGIN")) return Result.failure("Expected field \"UHF_PRE_COMM_MARGIN\", but not found: " + serializedValue);
    final var uhf_pre_comm_margin$ = durationValueMapper.deserializeValue(map.get("UHF_PRE_COMM_MARGIN"));
    if (uhf_pre_comm_margin$.getKind() == Result.Kind.Failure) return Result.failure(uhf_pre_comm_margin$.getFailureOrThrow());
    final var uhf_pre_comm_margin = uhf_pre_comm_margin$.getSuccessOrThrow();

    if (!map.containsKey("UHF_POST_COMM_MARGIN")) return Result.failure("Expected field \"UHF_POST_COMM_MARGIN\", but not found: " + serializedValue);
    final var uhf_post_comm_margin$ = durationValueMapper.deserializeValue(map.get("UHF_POST_COMM_MARGIN"));
    if (uhf_post_comm_margin$.getKind() == Result.Kind.Failure) return Result.failure(uhf_post_comm_margin$.getFailureOrThrow());
    final var uhf_post_comm_margin = uhf_post_comm_margin$.getSuccessOrThrow();

    if (!map.containsKey("XBAND_PREP_OVERHEAD")) return Result.failure("Expected field \"XBAND_PREP_OVERHEAD\", but not found: " + serializedValue);
    final var xband_prep_overhead$ = durationValueMapper.deserializeValue(map.get("XBAND_PREP_OVERHEAD"));
    if (xband_prep_overhead$.getKind() == Result.Kind.Failure) return Result.failure(xband_prep_overhead$.getFailureOrThrow());
    final var xband_prep_overhead = xband_prep_overhead$.getSuccessOrThrow();

    if (!map.containsKey("XBAND_CLEANUP_DURATION")) return Result.failure("Expected field \"XBAND_CLEANUP_DURATION\", but not found: " + serializedValue);
    final var xband_cleanup_duration$ = durationValueMapper.deserializeValue(map.get("XBAND_CLEANUP_DURATION"));
    if (xband_cleanup_duration$.getKind() == Result.Kind.Failure) return Result.failure(xband_cleanup_duration$.getFailureOrThrow());
    final var xband_cleanup_duration = xband_cleanup_duration$.getSuccessOrThrow();

    if (!map.containsKey("UHF_RT_DUR_1")) return Result.failure("Expected field \"UHF_RT_DUR_1\", but not found: " + serializedValue);
    final var uhf_rt_dur_1$ = durationValueMapper.deserializeValue(map.get("UHF_RT_DUR_1"));
    if (uhf_rt_dur_1$.getKind() == Result.Kind.Failure) return Result.failure(uhf_rt_dur_1$.getFailureOrThrow());
    final var uhf_rt_dur_1 = uhf_rt_dur_1$.getSuccessOrThrow();

    if (!map.containsKey("UHF_SCI_DUR_2_OFFSET")) return Result.failure("Expected field \"UHF_SCI_DUR_2_OFFSET\", but not found: " + serializedValue);
    final var uhf_sci_dur_2_offset$ = durationValueMapper.deserializeValue(map.get("UHF_SCI_DUR_2_OFFSET"));
    if (uhf_sci_dur_2_offset$.getKind() == Result.Kind.Failure) return Result.failure(uhf_sci_dur_2_offset$.getFailureOrThrow());
    final var uhf_sci_dur_2_offset = uhf_sci_dur_2_offset$.getSuccessOrThrow();

    if (!map.containsKey("UHF_RT_3_DUR")) return Result.failure("Expected field \"UHF_RT_3_DUR\", but not found: " + serializedValue);
    final var uhf_rt_3_dur$ = durationValueMapper.deserializeValue(map.get("UHF_RT_3_DUR"));
    if (uhf_rt_3_dur$.getKind() == Result.Kind.Failure) return Result.failure(uhf_rt_3_dur$.getFailureOrThrow());
    final var uhf_rt_3_dur = uhf_rt_3_dur$.getSuccessOrThrow();

    if (!map.containsKey("UHF_CLEANUP_DUR")) return Result.failure("Expected field \"UHF_CLEANUP_DUR\", but not found: " + serializedValue);
    final var uhf_cleanup_dur$ = durationValueMapper.deserializeValue(map.get("UHF_CLEANUP_DUR"));
    if (uhf_cleanup_dur$.getKind() == Result.Kind.Failure) return Result.failure(uhf_cleanup_dur$.getFailureOrThrow());
    final var uhf_cleanup_dur = uhf_cleanup_dur$.getSuccessOrThrow();

    if (!map.containsKey("UHF_RETURN_LINK_UNENCODED_EFFICIENCY")) return Result.failure("Expected field \"UHF_RETURN_LINK_UNENCODED_EFFICIENCY\", but not found: " + serializedValue);
    final var uhf_return_link_unencoded_efficiency$ = doubleValueMapper.deserializeValue(map.get("UHF_RETURN_LINK_UNENCODED_EFFICIENCY"));
    if (uhf_return_link_unencoded_efficiency$.getKind() == Result.Kind.Failure) return Result.failure(uhf_return_link_unencoded_efficiency$.getFailureOrThrow());
    final var uhf_return_link_unencoded_efficiency = uhf_return_link_unencoded_efficiency$.getSuccessOrThrow();

    if (!map.containsKey("UHF_RETURN_LINK_ENCODED_EFFICIENCY")) return Result.failure("Expected field \"UHF_RETURN_LINK_ENCODED_EFFICIENCY\", but not found: " + serializedValue);
    final var uhf_return_link_encoded_efficiency$ = doubleValueMapper.deserializeValue(map.get("UHF_RETURN_LINK_ENCODED_EFFICIENCY"));
    if (uhf_return_link_encoded_efficiency$.getKind() == Result.Kind.Failure) return Result.failure(uhf_return_link_encoded_efficiency$.getFailureOrThrow());
    final var uhf_return_link_encoded_efficiency = uhf_return_link_encoded_efficiency$.getSuccessOrThrow();

    if (!map.containsKey("UHF_DATA_VOLUME_SCALAR")) return Result.failure("Expected field \"UHF_DATA_VOLUME_SCALAR\", but not found: " + serializedValue);
    final var uhf_data_volume_scalar$ = doubleValueMapper.deserializeValue(map.get("UHF_DATA_VOLUME_SCALAR"));
    if (uhf_data_volume_scalar$.getKind() == Result.Kind.Failure) return Result.failure(uhf_data_volume_scalar$.getFailureOrThrow());
    final var uhf_data_volume_scalar = uhf_data_volume_scalar$.getSuccessOrThrow();

    if (!map.containsKey("UHF_DELAY_FOR_VC00")) return Result.failure("Expected field \"UHF_DELAY_FOR_VC00\", but not found: " + serializedValue);
    final var uhf_delay_for_vc00$ = booleanValueMapper.deserializeValue(map.get("UHF_DELAY_FOR_VC00"));
    if (uhf_delay_for_vc00$.getKind() == Result.Kind.Failure) return Result.failure(uhf_delay_for_vc00$.getFailureOrThrow());
    final var uhf_delay_for_vc00 = uhf_delay_for_vc00$.getSuccessOrThrow();

    return Result.success(new CommParameters(
        xband_pre_comm_margin,
        xband_post_comm_margin,
        uhf_pre_comm_margin,
        uhf_post_comm_margin,
        xband_prep_overhead,
        xband_cleanup_duration,
        uhf_rt_dur_1,
        uhf_sci_dur_2_offset,
        uhf_rt_3_dur,
        uhf_cleanup_dur,
        uhf_return_link_unencoded_efficiency,
        uhf_return_link_encoded_efficiency,
        uhf_data_volume_scalar,
        uhf_delay_for_vc00
    ));
  }

  @Override
  public SerializedValue serializeValue(final CommParameters value) {
    return SerializedValue.of(Map.ofEntries(
        Map.entry("XBAND_PRE_COMM_MARGIN", durationValueMapper.serializeValue(value.XBAND_PRE_COMM_MARGIN())),
        Map.entry("XBAND_POST_COMM_MARGIN", durationValueMapper.serializeValue(value.XBAND_POST_COMM_MARGIN())),
        Map.entry("UHF_PRE_COMM_MARGIN", durationValueMapper.serializeValue(value.UHF_PRE_COMM_MARGIN())),
        Map.entry("UHF_POST_COMM_MARGIN", durationValueMapper.serializeValue(value.UHF_POST_COMM_MARGIN())),
        Map.entry("XBAND_PREP_OVERHEAD", durationValueMapper.serializeValue(value.XBAND_PREP_OVERHEAD())),
        Map.entry("XBAND_CLEANUP_DURATION", durationValueMapper.serializeValue(value.XBAND_CLEANUP_DURATION())),
        Map.entry("UHF_RT_DUR_1", durationValueMapper.serializeValue(value.UHF_RT_DUR_1())),
        Map.entry("UHF_SCI_DUR_2_OFFSET", durationValueMapper.serializeValue(value.UHF_SCI_DUR_2_OFFSET())),
        Map.entry("UHF_RT_3_DUR", durationValueMapper.serializeValue(value.UHF_RT_3_DUR())),
        Map.entry("UHF_CLEANUP_DUR", durationValueMapper.serializeValue(value.UHF_CLEANUP_DUR())),
        Map.entry("UHF_RETURN_LINK_UNENCODED_EFFICIENCY", SerializedValue.of(value.UHF_RETURN_LINK_UNENCODED_EFFICIENCY())),
        Map.entry("UHF_RETURN_LINK_ENCODED_EFFICIENCY", SerializedValue.of(value.UHF_RETURN_LINK_ENCODED_EFFICIENCY())),
        Map.entry("UHF_DATA_VOLUME_SCALAR", SerializedValue.of(value.UHF_DATA_VOLUME_SCALAR())),
        Map.entry("UHF_DELAY_FOR_VC00", SerializedValue.of(value.UHF_DELAY_FOR_VC00()))
    ));
  }
}
