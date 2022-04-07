package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DurationValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.SchedulingParams;

import java.util.Map;

public final class SchedulingParamsMapper implements ValueMapper<SchedulingParams> {
  private final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();
  private final ValueMapper<Duration> durationValueMapper = new DurationValueMapper();
  private final ValueMapper<Boolean> booleanValueMapper = new BooleanValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.ofEntries(
        Map.entry("SEIS_SCI_RUNOUT_FRAC", ValueSchema.REAL),
        Map.entry("APSS_SCI_RUNOUT_FRAC", ValueSchema.REAL),
        Map.entry("HeatProbe_SCI_RUNOUT_FRAC", ValueSchema.REAL),
        Map.entry("MAX_RUNOUT_SEIS_SCI_PROC_DUR", ValueSchema.DURATION),
        Map.entry("MIN_RUNOUT_SEIS_SCI_PROC_DUR", ValueSchema.DURATION),
        Map.entry("MAX_RUNOUT_APSS_SCI_PROC_DUR", ValueSchema.DURATION),
        Map.entry("MIN_RUNOUT_APSS_SCI_PROC_DUR", ValueSchema.DURATION),
        Map.entry("MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR", ValueSchema.DURATION),
        Map.entry("MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR", ValueSchema.DURATION),
        Map.entry("RUNOUT_CLEANUP_MARGIN", ValueSchema.DURATION),
        Map.entry("MIN_SUB_RUNOUT_DUR", ValueSchema.DURATION),
        Map.entry("MinSleepDuration", ValueSchema.DURATION),
        Map.entry("MaxSleepDuration", ValueSchema.DURATION),
        Map.entry("PlacePostUplinkWake", ValueSchema.BOOLEAN)));
  }

  @Override
  public Result<SchedulingParams, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("SEIS_SCI_RUNOUT_FRAC")) return Result.failure("Expected field \"SEIS_SCI_RUNOUT_FRAC\", but not found: " + serializedValue);
    final var seis_sci_runout_frac$ = doubleValueMapper.deserializeValue(map.get("SEIS_SCI_RUNOUT_FRAC"));
    if (seis_sci_runout_frac$.getKind() == Result.Kind.Failure) return Result.failure(seis_sci_runout_frac$.getFailureOrThrow());
    final var seis_sci_runout_frac = seis_sci_runout_frac$.getSuccessOrThrow();

    if (!map.containsKey("APSS_SCI_RUNOUT_FRAC")) return Result.failure("Expected field \"APSS_SCI_RUNOUT_FRAC\", but not found: " + serializedValue);
    final var apss_sci_runout_frac$ = doubleValueMapper.deserializeValue(map.get("APSS_SCI_RUNOUT_FRAC"));
    if (apss_sci_runout_frac$.getKind() == Result.Kind.Failure) return Result.failure(apss_sci_runout_frac$.getFailureOrThrow());
    final var apss_sci_runout_frac = apss_sci_runout_frac$.getSuccessOrThrow();

    if (!map.containsKey("HeatProbe_SCI_RUNOUT_FRAC")) return Result.failure("Expected field \"HeatProbe_SCI_RUNOUT_FRAC\", but not found: " + serializedValue);
    final var HeatProbe_sci_runout_frac$ = doubleValueMapper.deserializeValue(map.get("HeatProbe_SCI_RUNOUT_FRAC"));
    if (HeatProbe_sci_runout_frac$.getKind() == Result.Kind.Failure) return Result.failure(HeatProbe_sci_runout_frac$.getFailureOrThrow());
    final var HeatProbe_sci_runout_frac = HeatProbe_sci_runout_frac$.getSuccessOrThrow();

    if (!map.containsKey("MAX_RUNOUT_SEIS_SCI_PROC_DUR")) return Result.failure("Expected field \"MAX_RUNOUT_SEIS_SCI_PROC_DUR\", but not found: " + serializedValue);
    final var max_runout_seis_sci_proc_dur$ = durationValueMapper.deserializeValue(map.get("MAX_RUNOUT_SEIS_SCI_PROC_DUR"));
    if (max_runout_seis_sci_proc_dur$.getKind() == Result.Kind.Failure) return Result.failure(max_runout_seis_sci_proc_dur$.getFailureOrThrow());
    final var max_runout_seis_sci_proc_dur = max_runout_seis_sci_proc_dur$.getSuccessOrThrow();

    if (!map.containsKey("MIN_RUNOUT_SEIS_SCI_PROC_DUR")) return Result.failure("Expected field \"MIN_RUNOUT_SEIS_SCI_PROC_DUR\", but not found: " + serializedValue);
    final var min_runout_seis_sci_proc_dur$ = durationValueMapper.deserializeValue(map.get("MIN_RUNOUT_SEIS_SCI_PROC_DUR"));
    if (min_runout_seis_sci_proc_dur$.getKind() == Result.Kind.Failure) return Result.failure(min_runout_seis_sci_proc_dur$.getFailureOrThrow());
    final var min_runout_seis_sci_proc_dur = min_runout_seis_sci_proc_dur$.getSuccessOrThrow();

    if (!map.containsKey("MAX_RUNOUT_APSS_SCI_PROC_DUR")) return Result.failure("Expected field \"MAX_RUNOUT_APSS_SCI_PROC_DUR\", but not found: " + serializedValue);
    final var max_runout_apss_sci_proc_dur$ = durationValueMapper.deserializeValue(map.get("MAX_RUNOUT_APSS_SCI_PROC_DUR"));
    if (max_runout_apss_sci_proc_dur$.getKind() == Result.Kind.Failure) return Result.failure(max_runout_apss_sci_proc_dur$.getFailureOrThrow());
    final var max_runout_apss_sci_proc_dur = max_runout_apss_sci_proc_dur$.getSuccessOrThrow();

    if (!map.containsKey("MIN_RUNOUT_APSS_SCI_PROC_DUR")) return Result.failure("Expected field \"MIN_RUNOUT_APSS_SCI_PROC_DUR\", but not found: " + serializedValue);
    final var min_runout_apss_sci_proc_dur$ = durationValueMapper.deserializeValue(map.get("MIN_RUNOUT_APSS_SCI_PROC_DUR"));
    if (min_runout_apss_sci_proc_dur$.getKind() == Result.Kind.Failure) return Result.failure(min_runout_apss_sci_proc_dur$.getFailureOrThrow());
    final var min_runout_apss_sci_proc_dur = min_runout_apss_sci_proc_dur$.getSuccessOrThrow();

    if (!map.containsKey("MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR")) return Result.failure("Expected field \"MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR\", but not found: " + serializedValue);
    final var max_runout_HeatProbe_get_scidata_dur$ = durationValueMapper.deserializeValue(map.get("MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR"));
    if (max_runout_HeatProbe_get_scidata_dur$.getKind() == Result.Kind.Failure) return Result.failure(max_runout_HeatProbe_get_scidata_dur$.getFailureOrThrow());
    final var max_runout_HeatProbe_get_scidata_dur = max_runout_HeatProbe_get_scidata_dur$.getSuccessOrThrow();

    if (!map.containsKey("MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR")) return Result.failure("Expected field \"MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR\", but not found: " + serializedValue);
    final var min_runout_HeatProbe_get_scidata_dur$ = durationValueMapper.deserializeValue(map.get("MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR"));
    if (min_runout_HeatProbe_get_scidata_dur$.getKind() == Result.Kind.Failure) return Result.failure(min_runout_HeatProbe_get_scidata_dur$.getFailureOrThrow());
    final var min_runout_HeatProbe_get_scidata_dur = min_runout_HeatProbe_get_scidata_dur$.getSuccessOrThrow();

    if (!map.containsKey("RUNOUT_CLEANUP_MARGIN")) return Result.failure("Expected field \"RUNOUT_CLEANUP_MARGIN\", but not found: " + serializedValue);
    final var runout_cleanup_margin$ = durationValueMapper.deserializeValue(map.get("RUNOUT_CLEANUP_MARGIN"));
    if (runout_cleanup_margin$.getKind() == Result.Kind.Failure) return Result.failure(runout_cleanup_margin$.getFailureOrThrow());
    final var runout_cleanup_margin = runout_cleanup_margin$.getSuccessOrThrow();

    if (!map.containsKey("MIN_SUB_RUNOUT_DUR")) return Result.failure("Expected field \"MIN_SUB_RUNOUT_DUR\", but not found: " + serializedValue);
    final var min_sub_runout_dur$ = durationValueMapper.deserializeValue(map.get("MIN_SUB_RUNOUT_DUR"));
    if (min_sub_runout_dur$.getKind() == Result.Kind.Failure) return Result.failure(min_sub_runout_dur$.getFailureOrThrow());
    final var min_sub_runout_dur = min_sub_runout_dur$.getSuccessOrThrow();

    if (!map.containsKey("MinSleepDuration")) return Result.failure("Expected field \"MinSleepDuration\", but not found: " + serializedValue);
    final var minSleepDuration$ = durationValueMapper.deserializeValue(map.get("MinSleepDuration"));
    if (minSleepDuration$.getKind() == Result.Kind.Failure) return Result.failure(minSleepDuration$.getFailureOrThrow());
    final var minSleepDuration = minSleepDuration$.getSuccessOrThrow();

    if (!map.containsKey("MaxSleepDuration")) return Result.failure("Expected field \"MaxSleepDuration\", but not found: " + serializedValue);
    final var maxSleepDuration$ = durationValueMapper.deserializeValue(map.get("MaxSleepDuration"));
    if (maxSleepDuration$.getKind() == Result.Kind.Failure) return Result.failure(maxSleepDuration$.getFailureOrThrow());
    final var maxSleepDuration = maxSleepDuration$.getSuccessOrThrow();

    if (!map.containsKey("PlacePostUplinkWake")) return Result.failure("Expected field \"PlacePostUplinkWake\", but not found: " + serializedValue);
    final var placePostUplinkWake$ = booleanValueMapper.deserializeValue(map.get("PlacePostUplinkWake"));
    if (placePostUplinkWake$.getKind() == Result.Kind.Failure) return Result.failure(placePostUplinkWake$.getFailureOrThrow());
    final var placePostUplinkWake = placePostUplinkWake$.getSuccessOrThrow();

    return Result.success(new SchedulingParams(
        seis_sci_runout_frac,
        apss_sci_runout_frac,
        HeatProbe_sci_runout_frac,
        max_runout_seis_sci_proc_dur,
        min_runout_seis_sci_proc_dur,
        max_runout_apss_sci_proc_dur,
        min_runout_apss_sci_proc_dur,
        max_runout_HeatProbe_get_scidata_dur,
        min_runout_HeatProbe_get_scidata_dur,
        runout_cleanup_margin,
        min_sub_runout_dur,
        minSleepDuration,
        maxSleepDuration,
        placePostUplinkWake
    ));
  }

  @Override
  public SerializedValue serializeValue(final SchedulingParams value) {
    return SerializedValue.of(Map.ofEntries(
        Map.entry("SEIS_SCI_RUNOUT_FRAC", SerializedValue.of(value.SEIS_SCI_RUNOUT_FRAC())),
        Map.entry("APSS_SCI_RUNOUT_FRAC", SerializedValue.of(value.APSS_SCI_RUNOUT_FRAC())),
        Map.entry("HeatProbe_SCI_RUNOUT_FRAC", SerializedValue.of(value.HeatProbe_SCI_RUNOUT_FRAC())),
        Map.entry("MAX_RUNOUT_SEIS_SCI_PROC_DUR", SerializedValue.of(value.MAX_RUNOUT_SEIS_SCI_PROC_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MIN_RUNOUT_SEIS_SCI_PROC_DUR", SerializedValue.of(value.MIN_RUNOUT_SEIS_SCI_PROC_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MAX_RUNOUT_APSS_SCI_PROC_DUR", SerializedValue.of(value.MAX_RUNOUT_APSS_SCI_PROC_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MIN_RUNOUT_APSS_SCI_PROC_DUR", SerializedValue.of(value.MIN_RUNOUT_APSS_SCI_PROC_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR", SerializedValue.of(value.MAX_RUNOUT_HeatProbe_GET_SCIDATA_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR", SerializedValue.of(value.MIN_RUNOUT_HeatProbe_GET_SCIDATA_DUR().in(Duration.MICROSECONDS))),
        Map.entry("RUNOUT_CLEANUP_MARGIN", SerializedValue.of(value.RUNOUT_CLEANUP_MARGIN().in(Duration.MICROSECONDS))),
        Map.entry("MIN_SUB_RUNOUT_DUR", SerializedValue.of(value.MIN_SUB_RUNOUT_DUR().in(Duration.MICROSECONDS))),
        Map.entry("MinSleepDuration", SerializedValue.of(value.MinSleepDuration().in(Duration.MICROSECONDS))),
        Map.entry("MaxSleepDuration", SerializedValue.of(value.MaxSleepDuration().in(Duration.MICROSECONDS))),
        Map.entry("PlacePostUplinkWake", SerializedValue.of(value.PlacePostUplinkWake()))));
  }
}
