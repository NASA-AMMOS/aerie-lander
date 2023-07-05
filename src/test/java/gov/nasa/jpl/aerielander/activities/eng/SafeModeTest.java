package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.eng.EngModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;


import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SafeModeTest {

  private final Mission mission;

  public SafeModeTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testToggleSafeMode() {
    final var safeModes = mission.engModel.safeModeMap;

    spawn(this.mission, new EnterSafeMode(Duration.MINUTE, EngModel.Component.APSS));

    delay(Duration.MINUTE);
    assertThat(safeModes.get(EngModel.Component.APSS).get()).isEqualTo(true);

    spawn(this.mission, new ExitSafeMode(Duration.MINUTE, EngModel.Component.APSS));

    delay(Duration.MINUTE);
    assertThat(safeModes.get(EngModel.Component.APSS).get()).isEqualTo(false);
  }
}
