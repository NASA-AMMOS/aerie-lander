package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DurationValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.MasterActivityDurations;

import java.util.Map;

public final class MasterActivityDurationsMapper implements ValueMapper<MasterActivityDurations> {
  private final ValueMapper<Duration> durationValueMapper = new DurationValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.ofEntries(
        Map.entry("GV_WAKEUP_TIME_OFFSET", ValueSchema.DURATION),
        Map.entry("BOOT_INIT_DURATION", ValueSchema.DURATION),
        Map.entry("LOAD_BLOCK_LIB_DURATION", ValueSchema.DURATION),
        Map.entry("WAKEUP_FULL_DURATION", ValueSchema.DURATION),
        Map.entry("WAKEUP_DIAG_DURATION", ValueSchema.DURATION),
        Map.entry("FILE_MGMT_DURATION", ValueSchema.DURATION),
        Map.entry("LME_CURVE_SEL_DURATION", ValueSchema.DURATION),
        Map.entry("SUBMASTER_DIAG_DURATION", ValueSchema.DURATION),
        Map.entry("FSW_DIAG_DURATION", ValueSchema.DURATION),
        Map.entry("FILE_COPY_DURATION", ValueSchema.DURATION),
        Map.entry("SHUTDOWN_FULL_DURATION", ValueSchema.DURATION),
        Map.entry("SHUTDOWN_DIAG_DURATION", ValueSchema.DURATION)
    ));
  }

  @Override
  public Result<MasterActivityDurations, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("GV_WAKEUP_TIME_OFFSET")) return Result.failure("Expected field \"GV_WAKEUP_TIME_OFFSET\", but not found: " + serializedValue);
    final var gv_wakeup_time_offset$ = durationValueMapper.deserializeValue(map.get("GV_WAKEUP_TIME_OFFSET"));
    if (gv_wakeup_time_offset$.getKind() == Result.Kind.Failure) return Result.failure(gv_wakeup_time_offset$.getFailureOrThrow());
    final var gv_wakeup_time_offset = gv_wakeup_time_offset$.getSuccessOrThrow();

    if (!map.containsKey("BOOT_INIT_DURATION")) return Result.failure("Expected field \"BOOT_INIT_DURATION\", but not found: " + serializedValue);
    final var boot_init_duration$ = durationValueMapper.deserializeValue(map.get("BOOT_INIT_DURATION"));
    if (boot_init_duration$.getKind() == Result.Kind.Failure) return Result.failure(boot_init_duration$.getFailureOrThrow());
    final var boot_init_duration = boot_init_duration$.getSuccessOrThrow();

    if (!map.containsKey("LOAD_BLOCK_LIB_DURATION")) return Result.failure("Expected field \"LOAD_BLOCK_LIB_DURATION\", but not found: " + serializedValue);
    final var load_block_lib_duration$ = durationValueMapper.deserializeValue(map.get("LOAD_BLOCK_LIB_DURATION"));
    if (load_block_lib_duration$.getKind() == Result.Kind.Failure) return Result.failure(load_block_lib_duration$.getFailureOrThrow());
    final var load_block_lib_duration = load_block_lib_duration$.getSuccessOrThrow();

    if (!map.containsKey("WAKEUP_FULL_DURATION")) return Result.failure("Expected field \"WAKEUP_FULL_DURATION\", but not found: " + serializedValue);
    final var wakeup_full_duration$ = durationValueMapper.deserializeValue(map.get("WAKEUP_FULL_DURATION"));
    if (wakeup_full_duration$.getKind() == Result.Kind.Failure) return Result.failure(wakeup_full_duration$.getFailureOrThrow());
    final var wakeup_full_duration = wakeup_full_duration$.getSuccessOrThrow();

    if (!map.containsKey("WAKEUP_DIAG_DURATION")) return Result.failure("Expected field \"WAKEUP_DIAG_DURATION\", but not found: " + serializedValue);
    final var wakeup_diag_duration$ = durationValueMapper.deserializeValue(map.get("WAKEUP_DIAG_DURATION"));
    if (wakeup_diag_duration$.getKind() == Result.Kind.Failure) return Result.failure(wakeup_diag_duration$.getFailureOrThrow());
    final var wakeup_diag_duration = wakeup_diag_duration$.getSuccessOrThrow();

    if (!map.containsKey("FILE_MGMT_DURATION")) return Result.failure("Expected field \"FILE_MGMT_DURATION\", but not found: " + serializedValue);
    final var file_mgmt_duration$ = durationValueMapper.deserializeValue(map.get("FILE_MGMT_DURATION"));
    if (file_mgmt_duration$.getKind() == Result.Kind.Failure) return Result.failure(file_mgmt_duration$.getFailureOrThrow());
    final var file_mgmt_duration = file_mgmt_duration$.getSuccessOrThrow();

    if (!map.containsKey("LME_CURVE_SEL_DURATION")) return Result.failure("Expected field \"LME_CURVE_SEL_DURATION\", but not found: " + serializedValue);
    final var lme_curve_sel_duration$ = durationValueMapper.deserializeValue(map.get("LME_CURVE_SEL_DURATION"));
    if (lme_curve_sel_duration$.getKind() == Result.Kind.Failure) return Result.failure(lme_curve_sel_duration$.getFailureOrThrow());
    final var lme_curve_sel_duration = lme_curve_sel_duration$.getSuccessOrThrow();

    if (!map.containsKey("SUBMASTER_DIAG_DURATION")) return Result.failure("Expected field \"SUBMASTER_DIAG_DURATION\", but not found: " + serializedValue);
    final var submaster_diag_duration$ = durationValueMapper.deserializeValue(map.get("SUBMASTER_DIAG_DURATION"));
    if (submaster_diag_duration$.getKind() == Result.Kind.Failure) return Result.failure(submaster_diag_duration$.getFailureOrThrow());
    final var submaster_diag_duration = submaster_diag_duration$.getSuccessOrThrow();

    if (!map.containsKey("FSW_DIAG_DURATION")) return Result.failure("Expected field \"FSW_DIAG_DURATION\", but not found: " + serializedValue);
    final var fsw_diag_duration$ = durationValueMapper.deserializeValue(map.get("FSW_DIAG_DURATION"));
    if (fsw_diag_duration$.getKind() == Result.Kind.Failure) return Result.failure(fsw_diag_duration$.getFailureOrThrow());
    final var fsw_diag_duration = fsw_diag_duration$.getSuccessOrThrow();

    if (!map.containsKey("FILE_COPY_DURATION")) return Result.failure("Expected field \"FILE_COPY_DURATION\", but not found: " + serializedValue);
    final var file_copy_duration$ = durationValueMapper.deserializeValue(map.get("FILE_COPY_DURATION"));
    if (file_copy_duration$.getKind() == Result.Kind.Failure) return Result.failure(file_copy_duration$.getFailureOrThrow());
    final var file_copy_duration = file_copy_duration$.getSuccessOrThrow();

    if (!map.containsKey("SHUTDOWN_FULL_DURATION")) return Result.failure("Expected field \"SHUTDOWN_FULL_DURATION\", but not found: " + serializedValue);
    final var shutdown_full_duration$ = durationValueMapper.deserializeValue(map.get("SHUTDOWN_FULL_DURATION"));
    if (shutdown_full_duration$.getKind() == Result.Kind.Failure) return Result.failure(shutdown_full_duration$.getFailureOrThrow());
    final var shutdown_full_duration = shutdown_full_duration$.getSuccessOrThrow();

    if (!map.containsKey("SHUTDOWN_DIAG_DURATION")) return Result.failure("Expected field \"SHUTDOWN_DIAG_DURATION\", but not found: " + serializedValue);
    final var shutdown_diag_duration$ = durationValueMapper.deserializeValue(map.get("SHUTDOWN_DIAG_DURATION"));
    if (shutdown_diag_duration$.getKind() == Result.Kind.Failure) return Result.failure(shutdown_diag_duration$.getFailureOrThrow());
    final var shutdown_diag_duration = shutdown_diag_duration$.getSuccessOrThrow();

    return Result.success(new MasterActivityDurations(
        gv_wakeup_time_offset,
        boot_init_duration,
        load_block_lib_duration,
        wakeup_full_duration,
        wakeup_diag_duration,
        file_mgmt_duration,
        lme_curve_sel_duration,
        submaster_diag_duration,
        fsw_diag_duration,
        file_copy_duration,
        shutdown_full_duration,
        shutdown_diag_duration
    ));
  }

  @Override
  public SerializedValue serializeValue(final MasterActivityDurations value) {
    return SerializedValue.of(Map.ofEntries(
        Map.entry("GV_WAKEUP_TIME_OFFSET", durationValueMapper.serializeValue(value.GV_WAKEUP_TIME_OFFSET())),
        Map.entry("BOOT_INIT_DURATION", durationValueMapper.serializeValue(value.BOOT_INIT_DURATION())),
        Map.entry("LOAD_BLOCK_LIB_DURATION", durationValueMapper.serializeValue(value.LOAD_BLOCK_LIB_DURATION())),
        Map.entry("WAKEUP_FULL_DURATION", durationValueMapper.serializeValue(value.WAKEUP_FULL_DURATION())),
        Map.entry("WAKEUP_DIAG_DURATION", durationValueMapper.serializeValue(value.WAKEUP_DIAG_DURATION())),
        Map.entry("FILE_MGMT_DURATION", durationValueMapper.serializeValue(value.FILE_MGMT_DURATION())),
        Map.entry("LME_CURVE_SEL_DURATION", durationValueMapper.serializeValue(value.LME_CURVE_SEL_DURATION())),
        Map.entry("SUBMASTER_DIAG_DURATION", durationValueMapper.serializeValue(value.SUBMASTER_DIAG_DURATION())),
        Map.entry("FSW_DIAG_DURATION", durationValueMapper.serializeValue(value.FSW_DIAG_DURATION())),
        Map.entry("FILE_COPY_DURATION", durationValueMapper.serializeValue(value.FILE_COPY_DURATION())),
        Map.entry("SHUTDOWN_FULL_DURATION", durationValueMapper.serializeValue(value.SHUTDOWN_FULL_DURATION())),
        Map.entry("SHUTDOWN_DIAG_DURATION", durationValueMapper.serializeValue(value.SHUTDOWN_DIAG_DURATION()))
    ));
  }
}
