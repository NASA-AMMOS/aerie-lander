package gov.nasa.jpl.aerielander.activities.apss;

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
public class APSSChangeAcqConfigTest {

  private final Mission mission;

  public APSSChangeAcqConfigTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testToggleComponents() {
    spawn(this.mission, new APSSChangeAcqConfig.Builder()
        .withTwinsPyOn(true)
        .withTwinsMyOn(true)
        .build());

    // TODO eventually make assertions that resources changed by this activity were changed as expected
  }
}
