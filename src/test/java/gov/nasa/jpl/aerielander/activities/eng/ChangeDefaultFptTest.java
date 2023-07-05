package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.models.data.DataConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;


import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeDefaultFptTest {

  private final Mission mission;

  public ChangeDefaultFptTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, Configuration.defaultConfiguration());
  }

  @Test
  public void testChangeDefault() {
    assertThat(mission.dataModel.defaultFPT.get()).isEqualTo(DataConfig.FPT.DEFAULT);

    spawn(this.mission, new ChangeDefaultFpt(DataConfig.FPT.dwn_fpt_HeatProbe));
    delay(Duration.MINUTE);
    assertThat(mission.dataModel.defaultFPT.get()).isEqualTo(DataConfig.FPT.dwn_fpt_HeatProbe);
  }
}
