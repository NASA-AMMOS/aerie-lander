package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;

import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;


import java.time.Instant;

import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TetherStorageBoxHeatersTest {

  private final Mission mission;

  public TetherStorageBoxHeatersTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testChassisToggle() {
    spawn(this.mission, new TetherStorageBoxChassisHeaterOn());
    // TODO assert mission.powerModel COVER PEL states were set

    spawn(this.mission, new TetherStorageBoxAllHeatersOff());
    // TODO assert mission.powerModel COVER PEL states were set
  }

  @Test
  public void testCoverToggle() {
    spawn(this.mission, new TetherStorageBoxChassisHeaterOn());
    // TODO assert mission.powerModel COVER PEL states were set

    spawn(this.mission, new TetherStorageBoxAllHeatersOff());
    // TODO assert mission.powerModel COVER PEL states were set
  }
}
