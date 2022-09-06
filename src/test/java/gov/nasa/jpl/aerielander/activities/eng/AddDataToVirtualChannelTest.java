package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.generated.GeneratedMissionModelFactory;
import gov.nasa.jpl.aerielander.models.data.DataConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddDataToVirtualChannelTest {

  @RegisterExtension
  public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

  private final Mission mission;

  public AddDataToVirtualChannelTest(final MerlinTestContext<ActivityTypes, Mission> ctx) {
    this.mission = new Mission(ctx.registrar(), Instant.EPOCH, Configuration.defaultConfiguration());
    ctx.use(mission, GeneratedMissionModelFactory.model);
  }

  @Test
  public void testAddData() {
    {
      final var chan = DataConfig.ChannelName.VC01;
      spawn(new AddDataToVirtualChannel(chan, 42.0));
      delay(Duration.of(1, MINUTES));
      assertThat(mission.dataModel.getChannel(chan).volume.get()).isEqualTo(42.0);
    }

    {
      final var chan = DataConfig.ChannelName.VC09;
      spawn(new AddDataToVirtualChannel(chan, 13.0));
      delay(Duration.of(1, MINUTES));
      assertThat(mission.dataModel.getChannel(chan).volume.get()).isEqualTo(13.0);
    }
  }
}
