package gov.nasa.jpl.aerielander;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.models.time.Clocks;
import gov.nasa.jpl.aerielander.models.time.Time;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MerlinExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public final class ClocksTest {
  private final Clocks clock = new Clocks(Time.Mars_Time_Origin);

  @Test
  public void testLmst() {
    assertEquals("Sol-0000M00:00:00.000000", clock.lmstTimestamp());

    delay(10, Duration.SECOND);
    assertEquals("Sol-0000M00:00:09.732443", clock.lmstTimestamp());

    delay(1, Duration.HOUR);
    assertEquals("Sol-0000M00:58:33.411915", clock.lmstTimestamp());

    delay(24, Duration.HOUR);
    assertEquals("Sol-0001M00:20:01.719236", clock.lmstTimestamp());
  }

  @Test
  public void testUtc() {
    assertEquals("2018-330T05:10:50.3356", clock.utcTimestamp());

    delay(10, Duration.SECOND);
    assertEquals("2018-330T05:11:00.3356", clock.utcTimestamp());

    delay(1, Duration.HOUR);
    assertEquals("2018-330T06:11:00.3356", clock.utcTimestamp());

    delay(24, Duration.HOUR);
    assertEquals("2018-331T06:11:00.3356", clock.utcTimestamp());
  }
}
