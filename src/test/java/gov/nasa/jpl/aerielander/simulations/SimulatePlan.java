package gov.nasa.jpl.aerielander.simulations;

import gov.nasa.jpl.aerie.merlin.driver.*;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.GeneratedModelType;
import gov.nasa.jpl.aerielander.parsers.ActivityInstance;
import gov.nasa.jpl.aerielander.parsers.MerlinParsers;
import gov.nasa.jpl.aerielander.parsers.NewPlan;

import javax.json.Json;
import javax.json.JsonValue;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public final class SimulatePlan {
  public static void main(final String[] args) {
    simulatePlan(new MissionModelBuilder(), "merlin-test-filtered.json", 1);
  }

  private static MissionModel<Mission> makeMissionModel(final MissionModelBuilder builder, final Configuration config) {
    final var factory = new GeneratedModelType();
    final var registry = DirectiveTypeRegistry.extract(factory);
    final var model = factory.instantiate(Instant.EPOCH, config, builder);
    return builder.build(model, registry);
  }

  public static void simulatePlan(final MissionModelBuilder builder, final String planName, final int numberSims) {
    final var buildStart = java.time.Instant.now();
    final var configuration = Configuration.defaultConfiguration();
    final var missionModel = makeMissionModel(builder, configuration);


    final var planJson = readPlan(planName);
    final var plan = MerlinParsers.newPlanP.parse(planJson).getSuccessOrThrow();
    final var schedule = buildScheduleFromPlan(plan);

    final var startTime = plan.startTimestamp.toInstant();
    final var endTime = plan.endTimestamp.toInstant();
    final var simulationDuration = durationBetween(startTime, endTime);

    final var buildEnd = java.time.Instant.now();
    final var prepTime = java.time.Duration.between(buildStart, buildEnd);
    System.out.println("Parse and Build time: " + prepTime);

    // Warmup
    for (int i = 0; i < 5; i++) {
      final var simulationStart = java.time.Instant.now();
      SimulationDriver.simulate(missionModel, schedule, startTime, simulationDuration, startTime, simulationDuration);
      final var simulationEnd = java.time.Instant.now();

      final var singleSimulationTime = java.time.Duration.between(simulationStart, simulationEnd);
      System.out.println("Warmup %s: %s".formatted(i, singleSimulationTime));
    }

    var totalSimulationTime = java.time.Duration.ZERO;
    for (int i = 0; i < numberSims; i++) {
      final var simulationStart = java.time.Instant.now();
      final var simulate = SimulationDriver.simulate(
          missionModel,
          schedule,
          startTime,
          simulationDuration,
          startTime,
          simulationDuration);
      final var simulationEnd = java.time.Instant.now();

      final var singleSimulationTime = java.time.Duration.between(simulationStart, simulationEnd);
      totalSimulationTime = totalSimulationTime.plus(singleSimulationTime);

      System.out.println("Simulation %s: %s".formatted(i, singleSimulationTime));
    }

    final var totalTime = prepTime.plus(totalSimulationTime);

    System.out.println("Average simulation time: " + totalSimulationTime.dividedBy(numberSims));
    System.out.println("Total time: " + totalTime);
  }

  public static Map<ActivityDirectiveId, ActivityDirective> buildScheduleFromPlan(final NewPlan plan) {
    final var startTime = plan.startTimestamp.toInstant();
    final var schedule = new HashMap<ActivityDirectiveId, ActivityDirective>();
    long counter = 0;

    for (final var instance : plan.activityInstances) {
      schedule.put(
              new ActivityDirectiveId(counter++),
              new ActivityDirective(Duration.of(startTime.until(
                              instance.startTimestamp.toInstant(),
                              ChronoUnit.MICROS),
                      Duration.MICROSECONDS), new SerializedActivity(instance.type, instance.parameters), null, true));
    }

    return schedule;
  }

  public static SerializedActivity serializeInstance(final ActivityInstance instance) {
    return new SerializedActivity(instance.type, instance.parameters);
  }

  public static JsonValue readPlan(final String planName) {
    final var path = "/gov/nasa/jpl/aerielander/" + planName;
    return Json.createReader(SimulatePlan.class.getResourceAsStream(path)).readValue();
  }

  public static Duration durationBetween(final Instant start, final Instant end) {
    final var dur = java.time.Duration.between(start, end);
    final var seconds = dur.getSeconds();
    final var nano = dur.getNano();
    return Duration.of(seconds, Duration.SECONDS).plus(nano/1000, Duration.MICROSECONDS);
  }
}
