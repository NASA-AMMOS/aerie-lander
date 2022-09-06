package gov.nasa.jpl.aerielander.parsers;

import gov.nasa.jpl.aerie.json.JsonParseResult;
import gov.nasa.jpl.aerie.json.JsonParser;
import gov.nasa.jpl.aerie.json.SchemaCache;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static gov.nasa.jpl.aerie.json.BasicParsers.listP;
import static gov.nasa.jpl.aerie.json.BasicParsers.mapP;
import static gov.nasa.jpl.aerie.json.BasicParsers.productP;
import static gov.nasa.jpl.aerie.json.BasicParsers.stringP;
import static gov.nasa.jpl.aerie.json.Uncurry.tuple;
import static gov.nasa.jpl.aerie.json.Uncurry.untuple;
import static gov.nasa.jpl.aerielander.parsers.SerializedValueJsonParser.serializedValueP;

public abstract class MerlinParsers {
    private MerlinParsers() {}
    public static final JsonParser<Timestamp> timestampP = new JsonParser<>() {
        @Override
        public JsonObject getSchema(final SchemaCache anchors) {
            return Json
                    .createObjectBuilder(stringP.getSchema())
                    .add("format", "date-time")
                    .build();
        }

        @Override
        public JsonParseResult<Timestamp> parse(final JsonValue json) {
            final var result = stringP.parse(json);
            if (result instanceof JsonParseResult.Success<String> s) {
                try {
                    return JsonParseResult.success(Timestamp.fromString(s.result()));
                } catch (DateTimeParseException e) {
                    return JsonParseResult.failure("invalid timestamp format");
                }
            } else if (result instanceof JsonParseResult.Failure<?> f) {
                return f.cast();
            } else {
                throw new RuntimeException("Unexcepted subtype");
                //throw new UnexpectedSubtypeError(JsonParseResult.class, result);
            }
        }

        @Override
        public JsonValue unparse(final Timestamp value) {
            return stringP.unparse(value.toString());
        }
    };

    public static final JsonParser<ActivityInstance> activityInstanceP
            = productP
            . field("type", stringP)
            . field("startTimestamp", timestampP)
            . field("parameters", mapP(serializedValueP))
            . map(untuple((type, startTimestamp, parameters) ->
                      new ActivityInstance(type, startTimestamp, parameters)),
                  activity -> tuple(activity.type, activity.startTimestamp, activity.parameters));

    public static final JsonParser<NewPlan> newPlanP
            = productP
            . field("name", stringP)
            . field("adaptationId", stringP)
            . field("startTimestamp", timestampP)
            . field("endTimestamp", timestampP)
            . optionalField("activityInstances", listP(activityInstanceP))
            . optionalField("configuration", mapP(serializedValueP))
            . map(untuple((name, adaptationId, startTimestamp, endTimestamp, activityInstances, configuration) ->
                      new NewPlan(name, adaptationId, startTimestamp, endTimestamp, activityInstances.orElse(List.of()), configuration.orElse(Map.of()))),
                  $ -> tuple($.name, $.adaptationId, $.startTimestamp, $.endTimestamp, Optional.of($.activityInstances), Optional.of($.configuration)));

}
