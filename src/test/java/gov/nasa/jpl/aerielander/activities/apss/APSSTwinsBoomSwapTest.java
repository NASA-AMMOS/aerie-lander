package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;


import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APSSTwinsBoomSwapTest {

  private final Mission mission;

  public APSSTwinsBoomSwapTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testTwinsBoomSwap() {
    final var model = mission.apssModel;
    final var twinsMyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_MY);
    final var twinsPyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_PY);

    spawn(this.mission, new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(false);
    assertThat(twinsPyOn.get()).isEqualTo(false);

    model.setComponentState(APSSModel.Component.TWINS_MY, true);
    model.setComponentState(APSSModel.Component.TWINS_PY, true);
    spawn(this.mission, new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(false);
    assertThat(twinsPyOn.get()).isEqualTo(true);

    model.setComponentState(APSSModel.Component.TWINS_MY, false);
    model.setComponentState(APSSModel.Component.TWINS_PY, true);
    spawn(this.mission, new APSSTwinsBoomSwap());
    delay(Duration.of(20, MINUTES));

    assertThat(twinsMyOn.get()).isEqualTo(true);
    assertThat(twinsPyOn.get()).isEqualTo(false);
  }
}
