package gov.nasa.jpl.aerielander.models.time;

import gov.nasa.jpl.aerie.contrib.models.Clock;
import gov.nasa.jpl.aerie.merlin.framework.Condition;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MILLISECONDS;

public final class Clocks {

  private final Time simStart;
  public final Clock utcClock;

  public Clocks(final Time now) {
    this.simStart = now;
    this.utcClock = new Clock(now.instant);
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.real(String.format("%s/utcClock/ticks", basePath), this.utcClock.ticks);
  }

  public Time getCurrentTime() {
    return new Time(this.utcClock.getTime());
  }

  public Duration timeUntil(final Time time) {
    return time.minus(this.getCurrentTime());
  }

  public Condition whenTimeIs(final Time time) {
    final var timeAsTicks = time.minus(simStart).in(MILLISECONDS);
    return this.utcClock.ticks.isBetween(timeAsTicks, timeAsTicks);
  }

  public Condition whenTimeIsReached(final Time time) {
    final var timeAsTicks = time.minus(simStart).in(MILLISECONDS);
    return this.utcClock.ticks.isBetween(timeAsTicks, Double.MAX_VALUE);
  }

  public String lmstTimestamp() {
    return this.getCurrentTime().lmstTimestamp();
  }

  public String utcTimestamp() {
    return this.getCurrentTime().utcTimestamp();
  }
}
