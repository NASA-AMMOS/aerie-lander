package gov.nasa.jpl.aerielander.mappers.config;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.ListValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.StringValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.config.OrbiterConfiguration;
import gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName;

import java.util.List;
import java.util.Map;

public final class OrbiterConfigurationMapper implements ValueMapper<OrbiterConfiguration> {
  final ValueMapper<String> stringValueMapper = new StringValueMapper();
  final ValueMapper<List<ChannelName>> channelListMapper = new ListValueMapper<>(new EnumValueMapper<>(ChannelName.class));
  final ValueMapper<Double> doubleValueMapper = new DoubleValueMapper();
  final ValueMapper<Boolean> booleanValueMapper = new BooleanValueMapper();

  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.ofStruct(Map.of(
        "blockName", stringValueMapper.getValueSchema(),
        "vcsDownlinked", channelListMapper.getValueSchema(),
        "dvAddedMbits", doubleValueMapper.getValueSchema(),
        "delayForVC00", booleanValueMapper.getValueSchema()
    ));
  }

  @Override
  public Result<OrbiterConfiguration, String> deserializeValue(final SerializedValue serializedValue) {
    final var map$ = serializedValue.asMap();
    if (map$.isEmpty()) return Result.failure("Expected map, got " + serializedValue);
    final var map = map$.orElseThrow();

    if (!map.containsKey("blockName")) return Result.failure("Expected field \"blockName\", but not found: " + serializedValue);
    final var blockName$ = stringValueMapper.deserializeValue(map.get("blockName"));
    if (blockName$.getKind() == Result.Kind.Failure) return Result.failure(blockName$.getFailureOrThrow());
    final var blockName = blockName$.getSuccessOrThrow();

    if (!map.containsKey("vcsDownlinked")) return Result.failure("Expected field \"vcsDownlinked\", but not found: " + serializedValue);
    final var vcsDownlinked$ = channelListMapper.deserializeValue(map.get("vcsDownlinked"));
    if (vcsDownlinked$.getKind() == Result.Kind.Failure) return Result.failure(vcsDownlinked$.getFailureOrThrow());
    final var vcsDownlinked = vcsDownlinked$.getSuccessOrThrow();

    if (!map.containsKey("dvAddedMbits")) return Result.failure("Expected field \"dvAddedMbits\", but not found: " + serializedValue);
    final var dvAddedMbits$ = doubleValueMapper.deserializeValue(map.get("dvAddedMbits"));
    if (dvAddedMbits$.getKind() == Result.Kind.Failure) return Result.failure(dvAddedMbits$.getFailureOrThrow());
    final var dvAddedMbits = dvAddedMbits$.getSuccessOrThrow();

    if (!map.containsKey("delayForVC00")) return Result.failure("Expected field \"delayForVC00\", but not found: " + serializedValue);
    final var delayForVC00$ = booleanValueMapper.deserializeValue(map.get("delayForVC00"));
    if (delayForVC00$.getKind() == Result.Kind.Failure) return Result.failure(delayForVC00$.getFailureOrThrow());
    final var delayForVC00 = delayForVC00$.getSuccessOrThrow();

    return Result.success(new OrbiterConfiguration(
       blockName,
       vcsDownlinked,
       dvAddedMbits,
       delayForVC00
    ));
  }

  @Override
  public SerializedValue serializeValue(final OrbiterConfiguration value) {
    return SerializedValue.of(Map.of(
        "blockName", stringValueMapper.serializeValue(value.blockName()),
        "vcsDownlinked", channelListMapper.serializeValue(value.vcsDownlinked()),
        "dvAddedMbits", doubleValueMapper.serializeValue(value.dvAddedMbits()),
        "delayForVC00", booleanValueMapper.serializeValue(value.delayForVC00())
    ));
  }
}
