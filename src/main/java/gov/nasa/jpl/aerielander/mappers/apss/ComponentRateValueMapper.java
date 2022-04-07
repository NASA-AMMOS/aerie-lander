package gov.nasa.jpl.aerielander.mappers.apss;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;

import java.util.Map;

public final class ComponentRateValueMapper implements ValueMapper<APSSModel.ComponentRate> {
  private final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.STRING;
  }

  @Override
  public Result<APSSModel.ComponentRate, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("defaultRate")) return Result.failure("Expected field \"defaultRate\", but not found: " + serializedValue);
    final var defaultRate$ = doubleValueMapper.deserializeValue(map.get("defaultRate"));
    if (defaultRate$.getKind() == Result.Kind.Failure) return Result.failure(defaultRate$.getFailureOrThrow());
    final var defaultRate = defaultRate$.getSuccessOrThrow();

    if (!map.containsKey("bothBoomsOnRate")) return Result.failure("Expected field \"bothBoomsOnRate\", but not found: " + serializedValue);
    final var bothBoomsOnRate$ = doubleValueMapper.deserializeValue(map.get("bothBoomsOnRate"));
    if (bothBoomsOnRate$.getKind() == Result.Kind.Failure) return Result.failure(bothBoomsOnRate$.getFailureOrThrow());
    final var bothBoomsOnRate = bothBoomsOnRate$.getSuccessOrThrow();

    return Result.success(new APSSModel.ComponentRate(defaultRate, bothBoomsOnRate));
  }

  @Override
  public SerializedValue serializeValue(final APSSModel.ComponentRate value) {
    return SerializedValue.of(Map.of(
        "defaultRate", SerializedValue.of(value.defaultRate()),
        "bothBoomsOnRate", SerializedValue.of(value.bothBoomsOnRate())));
  }
}
