package gov.nasa.jpl.aerielander.activities.eng;

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
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.RETX;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RetransmitTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public RetransmitTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission);
  }

  @Test
  public void testRetransmit() {
    spawn(mission, new Retransmit(10.0));
    final var vc = mission.dataModel.getChannel(RETX);

    delay(Duration.of(1, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(2.0);
    assertThat(vc.volume.rate.get()).isEqualTo(2.0);

    delay(Duration.of(2, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(6.0);

    delay(Duration.of(3, SECONDS));
    assertThat(vc.volume.get()).isEqualTo(10.0);
    assertThat(vc.volume.rate.get()).isEqualTo(0.0);
    assertThat(vc.overflow.get()).isEqualTo(0.0);
  }
}
