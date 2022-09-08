package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.generated.GeneratedMissionModelFactory;
import gov.nasa.jpl.aerielander.models.comm.CommModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseAlternateUhfBlockTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public UseAlternateUhfBlockTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission, GeneratedMissionModelFactory.model);
  }

  @Test
  public void testUseBlock() {
    final var inUse = mission.commModel.getAlternateUhfBlockInUseMap();

    assertThat(inUse.get(CommModel.Orbiter.ODY).get()).isEqualTo(false);

    spawn(mission, new UseAlternateUhfBlock(CommModel.Orbiter.MRO, true));
    delay(Duration.of(2, MINUTES));
    assertThat(inUse.get(CommModel.Orbiter.MRO).get()).isEqualTo(true);

    spawn(mission, new UseAlternateUhfBlock(CommModel.Orbiter.MRO, false));
    delay(Duration.of(2, MINUTES));
    assertThat(inUse.get(CommModel.Orbiter.MRO).get()).isEqualTo(false);
  }
}
