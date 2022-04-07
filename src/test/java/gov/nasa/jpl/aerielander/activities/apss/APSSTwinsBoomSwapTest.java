package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APSSTwinsBoomSwapTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public APSSTwinsBoomSwapTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Configuration.defaultConfiguration());
    ctx.use(mission, ActivityTypes::register);
  }

  @Test
  public void testTwinsBoomSwap() {
    final var model = mission.apssModel;
    final var twinsMyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_MY);
    final var twinsPyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_PY);

    spawn(new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(false);
    assertThat(twinsPyOn.get()).isEqualTo(false);

    model.setComponentState(APSSModel.Component.TWINS_MY, true);
    model.setComponentState(APSSModel.Component.TWINS_PY, true);
    spawn(new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(false);
    assertThat(twinsPyOn.get()).isEqualTo(true);

    model.setComponentState(APSSModel.Component.TWINS_MY, false);
    model.setComponentState(APSSModel.Component.TWINS_PY, true);
    spawn(new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(true);
    assertThat(twinsPyOn.get()).isEqualTo(false);
  }
}
