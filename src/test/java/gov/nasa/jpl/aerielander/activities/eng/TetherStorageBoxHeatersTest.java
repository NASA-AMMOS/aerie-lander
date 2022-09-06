package gov.nasa.jpl.aerielander.activities.eng;

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

import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TetherStorageBoxHeatersTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public TetherStorageBoxHeatersTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission, GeneratedMissionModelFactory.model);
  }

  @Test
  public void testChassisToggle() {
    spawn(new TetherStorageBoxChassisHeaterOn());
    // TODO assert mission.powerModel COVER PEL states were set

    spawn(new TetherStorageBoxAllHeatersOff());
    // TODO assert mission.powerModel COVER PEL states were set
  }

  @Test
  public void testCoverToggle() {
    spawn(new TetherStorageBoxChassisHeaterOn());
    // TODO assert mission.powerModel COVER PEL states were set

    spawn(new TetherStorageBoxAllHeatersOff());
    // TODO assert mission.powerModel COVER PEL states were set
  }
}
