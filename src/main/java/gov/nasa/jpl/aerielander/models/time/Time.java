package gov.nasa.jpl.aerielander.models.time;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public final class Time {
  // This builder must be used to get optional subsecond values
  // See: https://stackoverflow.com/questions/30090710/java-8-datetimeformatter-parsing-for-optional-fractional-seconds-of-varying-sign
  public static final DateTimeFormatter utcFormat =
      new DateTimeFormatterBuilder().appendPattern("uuuu-DDD'T'HH:mm:ss")
                                    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true).toFormatter();


  public static final Time Mars_Time_Origin = Time.fromUTC("2018-330T05:10:50.3356");
  public static final double Mars_Time_Scale = 1.02749125;

  protected final Instant instant;

  protected Time(final Instant instant) {
    this.instant = instant;
  }

  public boolean isBefore(final Time other) {
    return this.instant.until(other.instant, ChronoUnit.MICROS) > 0;
  }

  public boolean isAfter(final Time other) {
    return this.instant.until(other.instant, ChronoUnit.MICROS) < 0;
  }

  public boolean noEarlierThan(final Time other) {
    return !this.isBefore(other);
  }

  public boolean noLaterThan(final Time other) {
    return !this.isAfter(other);
  }

  public boolean equals(final Time other) {
    return this.noEarlierThan(other) && this.noLaterThan(other);
  }

  public Time minus(final Duration duration) {
    return new Time(this.instant.minus(duration.dividedBy(Duration.MICROSECOND), ChronoUnit.MICROS));
  }

  public Time plus(final Duration duration) {
    return new Time(this.instant.plus(duration.dividedBy(Duration.MICROSECOND), ChronoUnit.MICROS));
  }

  public Duration minus(final Time other) {
    return Duration.of(other.instant.until(this.instant, ChronoUnit.MICROS), Duration.MICROSECONDS);
  }

  public static Time fromUTC(final String utcTimestamp) {
    final var instant = LocalDateTime.parse(utcTimestamp, Time.utcFormat).atZone(ZoneOffset.UTC).toInstant();
    return new Time(instant);
  }

  public String utcTimestamp() {
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC).format(Time.utcFormat);
  }

  public String lmstTimestamp() {
    final var offset = java.time.Duration.between(Mars_Time_Origin.instant, instant);
    final var fractionalSeconds = offset.getSeconds()/Mars_Time_Scale;
    final var nanoPart = (offset.getNano()/Mars_Time_Scale);

    var totalSeconds = (long)Math.floor(fractionalSeconds);
    var nanos = (fractionalSeconds - totalSeconds)*1_000_000_000 + nanoPart;
    if (nanos > 1_000_000_000) {
      totalSeconds += 1;
      nanos -= 1_000_000_000;
    }

    final var sols = (totalSeconds / (60*60*24));
    var remainder = totalSeconds % (60*60*24);
    final var hours = (remainder / (60*60));
    remainder = remainder % (60*60);
    final var minutes = (remainder / (60));
    remainder = remainder % (60);
    final var seconds = remainder;
    final var microseconds = Math.round(nanos/1000);

    return String.format("Sol-%04dM%02d:%02d:%02d.%06d", sols, hours, minutes, seconds, microseconds);
  }

  public int solNumber() {
    final var offset = java.time.Duration.between(Mars_Time_Origin.instant, instant);
    final var fractionalSeconds = offset.getSeconds()/Mars_Time_Scale;
    final var nanoPart = (offset.getNano()/Mars_Time_Scale);

    var totalSeconds = (long)Math.floor(fractionalSeconds);
    var nanos = (fractionalSeconds - totalSeconds)*1_000_000_000 + nanoPart;
    if (nanos > 1_000_000_000) {
      totalSeconds += 1;
      nanos -= 1_000_000_000;
    }

    return (int)(totalSeconds / (60*60*24));
  }
}
