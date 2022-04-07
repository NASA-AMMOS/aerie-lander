package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.EngDataParams;

import java.util.Map;

public final class EngDataParamsMapper implements ValueMapper<EngDataParams> {
  private final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of(
        "AWAKE_ENG_DATA_RATE", ValueSchema.REAL,
        "UHF_PREP_DATA_DUMP_DIAG", ValueSchema.REAL,
        "UHF_PREP_DATA_DUMP_NO_DIAG", ValueSchema.REAL,
        "UHF_ACTIVE_DATA_RATE", ValueSchema.REAL,
        "SHUTDOWN_DATA_DUMP", ValueSchema.REAL,
        "WAKEUP_FULL_DATA_DUMP", ValueSchema.REAL,
        "WAKEUP_DIAG_DATA_DUMP", ValueSchema.REAL));
  }

  @Override
  public Result<EngDataParams, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("AWAKE_ENG_DATA_RATE")) return Result.failure("Expected field \"AWAKE_ENG_DATA_RATE\", but not found: " + serializedValue);
    final var awake_eng_data_rate$ = doubleValueMapper.deserializeValue(map.get("AWAKE_ENG_DATA_RATE"));
    if (awake_eng_data_rate$.getKind() == Result.Kind.Failure) return Result.failure(awake_eng_data_rate$.getFailureOrThrow());
    final var awake_eng_data_rate = awake_eng_data_rate$.getSuccessOrThrow();

    if (!map.containsKey("UHF_PREP_DATA_DUMP_DIAG")) return Result.failure("Expected field \"UHF_PREP_DATA_DUMP_DIAG\", but not found: " + serializedValue);
    final var uhf_prep_data_dump_diag$ = doubleValueMapper.deserializeValue(map.get("UHF_PREP_DATA_DUMP_DIAG"));
    if (uhf_prep_data_dump_diag$.getKind() == Result.Kind.Failure) return Result.failure(uhf_prep_data_dump_diag$.getFailureOrThrow());
    final var uhf_prep_data_dump_diag = uhf_prep_data_dump_diag$.getSuccessOrThrow();

    if (!map.containsKey("UHF_PREP_DATA_DUMP_NO_DIAG")) return Result.failure("Expected field \"UHF_PREP_DATA_DUMP_NO_DIAG\", but not found: " + serializedValue);
    final var uhf_prep_data_dump_no_diag$ = doubleValueMapper.deserializeValue(map.get("UHF_PREP_DATA_DUMP_NO_DIAG"));
    if (uhf_prep_data_dump_no_diag$.getKind() == Result.Kind.Failure) return Result.failure(uhf_prep_data_dump_no_diag$.getFailureOrThrow());
    final var uhf_prep_data_dump_no_diag = uhf_prep_data_dump_no_diag$.getSuccessOrThrow();

    if (!map.containsKey("UHF_ACTIVE_DATA_RATE")) return Result.failure("Expected field \"UHF_ACTIVE_DATA_RATE\", but not found: " + serializedValue);
    final var uhf_active_data_rate$ = doubleValueMapper.deserializeValue(map.get("UHF_ACTIVE_DATA_RATE"));
    if (uhf_active_data_rate$.getKind() == Result.Kind.Failure) return Result.failure(uhf_active_data_rate$.getFailureOrThrow());
    final var uhf_active_data_rate = uhf_active_data_rate$.getSuccessOrThrow();

    if (!map.containsKey("SHUTDOWN_DATA_DUMP")) return Result.failure("Expected field \"SHUTDOWN_DATA_DUMP\", but not found: " + serializedValue);
    final var shutdown_data_dump$ = doubleValueMapper.deserializeValue(map.get("SHUTDOWN_DATA_DUMP"));
    if (shutdown_data_dump$.getKind() == Result.Kind.Failure) return Result.failure(shutdown_data_dump$.getFailureOrThrow());
    final var shutdown_data_dump = shutdown_data_dump$.getSuccessOrThrow();

    if (!map.containsKey("WAKEUP_FULL_DATA_DUMP")) return Result.failure("Expected field \"WAKEUP_FULL_DATA_DUMP\", but not found: " + serializedValue);
    final var wakeup_full_data_dump$ = doubleValueMapper.deserializeValue(map.get("WAKEUP_FULL_DATA_DUMP"));
    if (wakeup_full_data_dump$.getKind() == Result.Kind.Failure) return Result.failure(wakeup_full_data_dump$.getFailureOrThrow());
    final var wakeup_full_data_dump = wakeup_full_data_dump$.getSuccessOrThrow();

    if (!map.containsKey("WAKEUP_DIAG_DATA_DUMP")) return Result.failure("Expected field \"WAKEUP_DIAG_DATA_DUMP\", but not found: " + serializedValue);
    final var wakeup_diag_data_dump$ = doubleValueMapper.deserializeValue(map.get("WAKEUP_DIAG_DATA_DUMP"));
    if (wakeup_diag_data_dump$.getKind() == Result.Kind.Failure) return Result.failure(wakeup_diag_data_dump$.getFailureOrThrow());
    final var wakeup_diag_data_dump = wakeup_diag_data_dump$.getSuccessOrThrow();

    return Result.success(new EngDataParams(
        awake_eng_data_rate,
        uhf_prep_data_dump_diag,
        uhf_prep_data_dump_no_diag,
        uhf_active_data_rate,
        shutdown_data_dump,
        wakeup_full_data_dump,
        wakeup_diag_data_dump
    ));
  }

  @Override
  public SerializedValue serializeValue(final EngDataParams value) {
    return SerializedValue.of(Map.of(
        "AWAKE_ENG_DATA_RATE", SerializedValue.of(value.AWAKE_ENG_DATA_RATE()),
        "UHF_PREP_DATA_DUMP_DIAG", SerializedValue.of(value.UHF_PREP_DATA_DUMP_DIAG()),
        "UHF_PREP_DATA_DUMP_NO_DIAG", SerializedValue.of(value.UHF_PREP_DATA_DUMP_NO_DIAG()),
        "UHF_ACTIVE_DATA_RATE", SerializedValue.of(value.UHF_ACTIVE_DATA_RATE()),
        "SHUTDOWN_DATA_DUMP", SerializedValue.of(value.SHUTDOWN_DATA_DUMP()),
        "WAKEUP_FULL_DATA_DUMP", SerializedValue.of(value.WAKEUP_FULL_DATA_DUMP()),
        "WAKEUP_DIAG_DATA_DUMP", SerializedValue.of(value.WAKEUP_DIAG_DATA_DUMP())
    ));
  }
}
