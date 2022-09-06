package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.data.DataConfig.APID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EngGenericTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public EngGenericTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission, ActivityTypes::register);
  }

  @Test
  public void testEngGeneric() {
    spawn(new EngGeneric(1.0, 1.0));
    final var vc = mission.dataModel.getApidModel(APID.SPACECRAFT).get().getRoutedVirtualChannel();

    delay(Duration.of(1, SECONDS));
    assertThat(mission.powerModel.getGenericPowerUsed()).isEqualTo(900.0);
    assertThat(vc.volume.get()).isEqualTo(0.25);

    delay(Duration.of(2, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(0.75);

    delay(Duration.of(1, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(1.0);
    assertThat(vc.volume.rate.get()).isEqualTo(0.25);
    assertThat(vc.overflow.get()).isEqualTo(0.0);

    delay(Duration.of(1, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(1.0);
    assertThat(vc.volume.rate.get()).isEqualTo(0.0);
    assertThat(vc.limit).isEqualTo(240.0);
  }
}
