package gov.nasa.jpl.aerielander.mappers.seis;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;

import java.util.Map;

public final class ChannelRateValueMapper implements ValueMapper<SeisConfig.ChannelRate> {
  private final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of("inRate", ValueSchema.REAL, "outRate", ValueSchema.REAL));
  }

  @Override
  public Result<SeisConfig.ChannelRate, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("inRate")) return Result.failure("Expected field \"inRate\", but not found: " + serializedValue);
    final var inRate$ = doubleValueMapper.deserializeValue(map.get("inRate"));
    if (inRate$.getKind() == Result.Kind.Failure) return Result.failure(inRate$.getFailureOrThrow());
    final var inRate = inRate$.getSuccessOrThrow();

    if (!map.containsKey("outRate")) return Result.failure("Expected field \"outRate\", but not found: " + serializedValue);
    final var outRate$ = doubleValueMapper.deserializeValue(map.get("outRate"));
    if (outRate$.getKind() == Result.Kind.Failure) return Result.failure(outRate$.getFailureOrThrow());
    final var outRate = outRate$.getSuccessOrThrow();

    return Result.success(new SeisConfig.ChannelRate(inRate, outRate));
  }

  @Override
  public SerializedValue serializeValue(final SeisConfig.ChannelRate value) {
    return SerializedValue.of(Map.of(
        "inRate", SerializedValue.of(value.inRate()),
        "outRate", SerializedValue.of(value.outRate())));
  }
}
