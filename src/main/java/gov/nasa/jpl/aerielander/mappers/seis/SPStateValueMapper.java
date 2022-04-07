package gov.nasa.jpl.aerielander.mappers.seis;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.activities.seis.SPState;

import java.util.Map;

public final class SPStateValueMapper implements ValueMapper<SPState> {
  final ValueMapper<Boolean> booleanValueMapper = new BooleanValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of(
        "sp1on", ValueSchema.BOOLEAN,
        "sp2on", ValueSchema.BOOLEAN,
        "sp3on", ValueSchema.BOOLEAN
    ));
  }

  @Override
  public Result<SPState, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("sp1on")) return Result.failure("Expected field \"sp1on\", but not found: " + serializedValue);
    final var sp1on$ = booleanValueMapper.deserializeValue(map.get("sp1on"));
    if (sp1on$.getKind() == Result.Kind.Failure) return Result.failure(sp1on$.getFailureOrThrow());
    final var sp1on = sp1on$.getSuccessOrThrow();

    if (!map.containsKey("sp2on")) return Result.failure("Expected field \"sp1on\", but not found: " + serializedValue);
    final var sp2on$ = booleanValueMapper.deserializeValue(map.get("sp1on"));
    if (sp2on$.getKind() == Result.Kind.Failure) return Result.failure(sp1on$.getFailureOrThrow());
    final var sp2on = sp1on$.getSuccessOrThrow();

    if (!map.containsKey("sp3on")) return Result.failure("Expected field \"sp1on\", but not found: " + serializedValue);
    final var sp3on$ = booleanValueMapper.deserializeValue(map.get("sp1on"));
    if (sp3on$.getKind() == Result.Kind.Failure) return Result.failure(sp1on$.getFailureOrThrow());
    final var sp3on = sp1on$.getSuccessOrThrow();

    return Result.success(new SPState(sp1on, sp2on, sp3on));
  }

  @Override
  public SerializedValue serializeValue(final SPState value) {
    return SerializedValue.of(Map.of(
        "sp1on", SerializedValue.of(value.sp1on()),
        "sp2on", SerializedValue.of(value.sp2on()),
        "sp3on", SerializedValue.of(value.sp3on()
        )));
  }
}
