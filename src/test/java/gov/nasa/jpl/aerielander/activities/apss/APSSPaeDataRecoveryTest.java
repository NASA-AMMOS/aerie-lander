package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;

import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APSSPaeDataRecoveryTest {

  private final Mission mission;

  public APSSPaeDataRecoveryTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testPaeDataRecovery() {
    // TODO eventually make assertions that resources changed by this activity were changed as expected
  }
}
