package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.OrbiterConfiguration;
import gov.nasa.jpl.aerielander.config.OrbiterParams;

import java.util.Map;

public final class OrbiterParamsMapper implements ValueMapper<OrbiterParams> {
  private final ValueMapper<OrbiterConfiguration> orbiterConfigurationMapper = new OrbiterConfigurationMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of(
        "ODY", orbiterConfigurationMapper.getValueSchema(),
        "MRO", orbiterConfigurationMapper.getValueSchema(),
        "TGO", orbiterConfigurationMapper.getValueSchema(),
        "MVN", orbiterConfigurationMapper.getValueSchema(),
        "MEX", orbiterConfigurationMapper.getValueSchema()
    ));
  }

  @Override
  public Result<OrbiterParams, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("ODY")) return Result.failure("Expected field \"ODY\", but not found: " + serializedValue);
    final var odyParams$ = orbiterConfigurationMapper.deserializeValue(map.get("ODY"));
    if (odyParams$.getKind() == Result.Kind.Failure) return Result.failure(odyParams$.getFailureOrThrow());
    final var odyParams = odyParams$.getSuccessOrThrow();

    if (!map.containsKey("MRO")) return Result.failure("Expected field \"MRO\", but not found: " + serializedValue);
    final var mroParams$ = orbiterConfigurationMapper.deserializeValue(map.get("MRO"));
    if (mroParams$.getKind() == Result.Kind.Failure) return Result.failure(mroParams$.getFailureOrThrow());
    final var mroParams = mroParams$.getSuccessOrThrow();

    if (!map.containsKey("TGO")) return Result.failure("Expected field \"TGO\", but not found: " + serializedValue);
    final var tgoParams$ = orbiterConfigurationMapper.deserializeValue(map.get("TGO"));
    if (tgoParams$.getKind() == Result.Kind.Failure) return Result.failure(tgoParams$.getFailureOrThrow());
    final var tgoParams = tgoParams$.getSuccessOrThrow();

    if (!map.containsKey("MVN")) return Result.failure("Expected field \"MVN\", but not found: " + serializedValue);
    final var mvnParams$ = orbiterConfigurationMapper.deserializeValue(map.get("MVN"));
    if (mvnParams$.getKind() == Result.Kind.Failure) return Result.failure(mvnParams$.getFailureOrThrow());
    final var mvnParams = mvnParams$.getSuccessOrThrow();

    if (!map.containsKey("MEX")) return Result.failure("Expected field \"MEX\", but not found: " + serializedValue);
    final var mexParams$ = orbiterConfigurationMapper.deserializeValue(map.get("MEX"));
    if (mexParams$.getKind() == Result.Kind.Failure) return Result.failure(mexParams$.getFailureOrThrow());
    final var mexParams = mexParams$.getSuccessOrThrow();

    return Result.success(new OrbiterParams(
        odyParams,
        mroParams,
        tgoParams,
        mvnParams,
        mexParams
    ));
  }

  @Override
  public SerializedValue serializeValue(final OrbiterParams value) {
    return SerializedValue.of(Map.of(
        "ODY", orbiterConfigurationMapper.serializeValue(value.ODY()),
        "MRO", orbiterConfigurationMapper.serializeValue(value.MRO()),
        "TGO", orbiterConfigurationMapper.serializeValue(value.TGO()),
        "MVN", orbiterConfigurationMapper.serializeValue(value.MVN()),
        "MEX", orbiterConfigurationMapper.serializeValue(value.MEX())
    ));
  }
}
