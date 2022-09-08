package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_CONTINUOUS_SCI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_FSW_SPECIAL_EVR;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APSSGenericTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public APSSGenericTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission);
  }

  @Test
  public void testApssGeneric() {
    spawn(mission, new APSSGeneric(23.0, 42.0, 1.0, 2.0));

    delay(Duration.of(1, MINUTES));
    assertThat(mission.powerModel.getGenericPowerUsed()).isEqualTo(25.714285714285715);

    delay(Duration.of(7, MINUTES));
    assertThat(mission.powerModel.getGenericPowerUsed()).isEqualTo(1.0);
    final var contSciVc = mission.dataModel.getApidModel(APID_APSS_CONTINUOUS_SCI).get().getRoutedVirtualChannel();
    final var fswSpecialVc = mission.dataModel.getApidModel(APID_APSS_FSW_SPECIAL_EVR).get().getRoutedVirtualChannel();

    // Both APIDs share the same routed VC
    assertThat(contSciVc.volume.get()).isEqualTo(65.0);
    assertThat(fswSpecialVc.volume.get()).isEqualTo(65.0);
  }
}
