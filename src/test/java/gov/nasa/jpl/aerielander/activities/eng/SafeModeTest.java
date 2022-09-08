package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.eng.EngModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SafeModeTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public SafeModeTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission);
  }

  @Test
  public void testToggleSafeMode() {
    final var safeModes = mission.engModel.safeModeMap;

    spawn(mission, new EnterSafeMode(Duration.MINUTE, EngModel.Component.APSS));

    delay(Duration.MINUTE);
    assertThat(safeModes.get(EngModel.Component.APSS).get()).isEqualTo(true);

    spawn(mission, new ExitSafeMode(Duration.MINUTE, EngModel.Component.APSS));

    delay(Duration.MINUTE);
    assertThat(safeModes.get(EngModel.Component.APSS).get()).isEqualTo(false);
  }
}
