package gov.nasa.jpl.aerielander.mappers;

import gov.nasa.jpl.aerie.merlin.framework.Result;
import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerielander.models.time.Time;

import java.util.function.Function;

public final class TimeValueMapper implements ValueMapper<Time> {
  @Override
  public ValueSchema getValueSchema() {
    return ValueSchema.STRING;
  }

  @Override
  public Result<Time, String> deserializeValue(final SerializedValue serializedValue) {
    return serializedValue
        .asString()
        .map(Time::fromUTC)
        .map((Function<Time, Result<Time, String>>) Result::success)
        .orElseGet(() -> Result.failure("Expected string, got " + serializedValue.toString()));
  }

  @Override
  public SerializedValue serializeValue(final Time value) {
    return SerializedValue.of(value.utcTimestamp());
  }
}
