package gov.nasa.jpl.aerielander.mappers.seis;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.activities.seis.VBBState;

import java.util.Map;

public final class VBBStateValueMapper implements ValueMapper<VBBState> {
  final ValueMapper<Boolean> booleanValueMapper = new BooleanValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of(
        "vbb1on", ValueSchema.BOOLEAN,
        "vbb2on", ValueSchema.BOOLEAN,
        "vbb3on", ValueSchema.BOOLEAN
    ));
  }

  @Override
  public Result<VBBState, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("vbb1on")) return Result.failure("Expected field \"vbb1on\", but not found: " + serializedValue);
    final var vbb1on$ = booleanValueMapper.deserializeValue(map.get("vbb1on"));
    if (vbb1on$.getKind() == Result.Kind.Failure) return Result.failure(vbb1on$.getFailureOrThrow());
    final var vbb1on = vbb1on$.getSuccessOrThrow();

    if (!map.containsKey("vbb2on")) return Result.failure("Expected field \"vbb1on\", but not found: " + serializedValue);
    final var vbb2on$ = booleanValueMapper.deserializeValue(map.get("vbb1on"));
    if (vbb2on$.getKind() == Result.Kind.Failure) return Result.failure(vbb1on$.getFailureOrThrow());
    final var vbb2on = vbb1on$.getSuccessOrThrow();

    if (!map.containsKey("vbb3on")) return Result.failure("Expected field \"vbb1on\", but not found: " + serializedValue);
    final var vbb3on$ = booleanValueMapper.deserializeValue(map.get("vbb1on"));
    if (vbb3on$.getKind() == Result.Kind.Failure) return Result.failure(vbb1on$.getFailureOrThrow());
    final var vbb3on = vbb1on$.getSuccessOrThrow();

    return Result.success(new VBBState(vbb1on, vbb2on, vbb3on));
  }

  @Override
  public SerializedValue serializeValue(final VBBState value) {
    return SerializedValue.of(Map.of(
        "vbb1on", SerializedValue.of(value.vbb1on()),
        "vbb2on", SerializedValue.of(value.vbb2on()),
        "vbb3on", SerializedValue.of(value.vbb3on()
    )));
  }
}
