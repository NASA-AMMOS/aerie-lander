package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.generated.GeneratedMissionModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APSSProcessContinuousDataTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public APSSProcessContinuousDataTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission, GeneratedMissionModelFactory.model);
  }

  @Test
  public void testProcessContinuousData() {
    // TODO eventually make assertions that resources changed by this activity were changed as expected
  }
}
