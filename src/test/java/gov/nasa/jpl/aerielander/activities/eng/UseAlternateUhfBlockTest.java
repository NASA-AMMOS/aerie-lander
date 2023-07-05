package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;

import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.comm.CommModel;
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
public class UseAlternateUhfBlockTest {

  private final Mission mission;

  public UseAlternateUhfBlockTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testUseBlock() {
    final var inUse = mission.commModel.getAlternateUhfBlockInUseMap();

    assertThat(inUse.get(CommModel.Orbiter.ODY).get()).isEqualTo(false);

    spawn(this.mission, new UseAlternateUhfBlock(CommModel.Orbiter.MRO, true));
    delay(Duration.of(2, MINUTES));
    assertThat(inUse.get(CommModel.Orbiter.MRO).get()).isEqualTo(true);

    spawn(this.mission, new UseAlternateUhfBlock(CommModel.Orbiter.MRO, false));
    delay(Duration.of(2, MINUTES));
    assertThat(inUse.get(CommModel.Orbiter.MRO).get()).isEqualTo(false);
  }
}
