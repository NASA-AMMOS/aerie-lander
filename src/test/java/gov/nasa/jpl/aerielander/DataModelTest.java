package gov.nasa.jpl.aerielander;

import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinTestContext;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHFActive;
import gov.nasa.jpl.aerielander.config.CommParameters;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.config.EngDataParams;
import gov.nasa.jpl.aerielander.config.MasterActivityDurations;
import gov.nasa.jpl.aerielander.config.OrbiterConfiguration;
import gov.nasa.jpl.aerielander.config.OrbiterParams;
import gov.nasa.jpl.aerielander.config.SchedulingParams;
import gov.nasa.jpl.aerielander.generated.ActivityTypes;
import gov.nasa.jpl.aerielander.models.comm.CommModel;
import gov.nasa.jpl.aerielander.models.data.DataConfig;
import gov.nasa.jpl.aerielander.models.data.DataModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.VC00;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.VC01;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.VC02;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.VC06;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class DataModelTest {

  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  public @Nested final class DefaultConfigurationTests {

    @RegisterExtension
    public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

    private final Mission mission;
    private final DataModel dataModel;

    public DefaultConfigurationTests(final MerlinTestContext<ActivityTypes, Mission> ctx) {
      final var config = Configuration.defaultConfiguration();
      mission = new Mission(ctx.registrar(), Instant.EPOCH, config);
      ctx.use(mission);
      dataModel = mission.dataModel;
    }

    @Test
    public void testOverflow() {
      final var vc1 = dataModel.getChannel(VC01);
      final var vc6 = dataModel.getChannel(VC06);

      // The overflow mapping should never change. It is hardcoded and no activities exist to change it
      assertEquals(VC06, vc1.overflowChannelId);

      vc1.increaseDataRate(10.0);

      delay(5, SECONDS);
      assertWithinTolerance(50.0, vc1.volume.get());
      assertWithinTolerance(0.0, vc1.overflow.get());
      assertWithinTolerance(0.0, vc6.volume.get());

      delay(2, SECONDS);
      assertWithinTolerance(50.0, vc1.volume.get());
      assertWithinTolerance(20.0, vc1.overflow.get());
      assertWithinTolerance(20.0, vc6.volume.get());

      vc1.increaseDataRate(10.0);

      delay(1, SECONDS);
      assertWithinTolerance(50.0, vc1.volume.get());
      assertWithinTolerance(40.0, vc1.overflow.get());
      assertWithinTolerance(40.0, vc6.volume.get());

      vc1.increaseDataRate(-20.0);

      delay(10, SECONDS);
      assertWithinTolerance(50.0, vc1.volume.get());
      assertWithinTolerance(40.0, vc1.overflow.get());
      assertWithinTolerance(40.0, vc6.volume.get());

      vc1.increaseDataRate(-4);

      delay(5, SECONDS);
      assertWithinTolerance(30.0, vc1.volume.get());
      assertWithinTolerance(40.0, vc1.overflow.get());
      assertWithinTolerance(40.0, vc6.volume.get());
    }

    @Test
    public void testDiscarded() {
      final var vc6 = dataModel.getChannel(VC06);

      // The overflow mapping should never change. It is hardcoded and no activities exist to change it
      assertEquals(DataConfig.ChannelName.DISCARD, vc6.overflowChannelId);

      vc6.increaseDataRate(10.0);

      delay(5, SECONDS);
      assertWithinTolerance(50.0, vc6.volume.get());
      assertWithinTolerance(0.0, vc6.overflow.get());

      delay(5, SECONDS);
      assertWithinTolerance(50.0, vc6.volume.get());
      assertWithinTolerance(50.0, vc6.overflow.get());

      vc6.increaseDataRate(-20.0);

      delay(3, SECONDS);
      assertWithinTolerance(20.0, vc6.volume.get());
      assertWithinTolerance(50.0, vc6.overflow.get());
    }

    @Test
    public void testDownlinkVC00Delays() {
      final var vc00 = dataModel.getChannel(VC00);
      final var vc01 = dataModel.getChannel(VC01);
      final var vc02 = dataModel.getChannel(VC02);

      // Fill the vcs
      vc00.increaseDataRate(20);
      vc01.increaseDataRate(20);
      vc02.increaseDataRate(20);

      delay(1, Duration.SECOND);

      vc00.increaseDataRate(-20);
      vc01.increaseDataRate(-20);
      vc02.increaseDataRate(-20);

      spawn(mission, new UHFActive("ODY_TestID", 20, Duration.of(10, SECONDS)));

      // All channels should start with 20 MBits
      var expectedVC00data = 20.0;
      var expectedVC01data = 20.0;
      var expectedVC02data = 20.0;
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // First half of downlink should not skip VC00
      delay(5, SECONDS);
      expectedVC00data += -10 + 5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE();
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // Second half of downlink should start with VC00
      delay(5, SECONDS);
      expectedVC00data += -10 + (5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE());
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // After pass data volumes should be unaffected
      delay(5, SECONDS);
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());
    }
  }

  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  public @Nested final class ODYDelayVC00Tests {

    @RegisterExtension
    public static final MerlinExtension<ActivityTypes, Mission> ext = new MerlinExtension<>();

    private final Mission mission;
    private final DataModel dataModel;

    public ODYDelayVC00Tests(final MerlinTestContext<ActivityTypes, Mission> ctx) {
      final var channelsExcludingV01 = new ArrayList<>(List.of(DataConfig.ChannelName.values()));
      channelsExcludingV01.remove(VC01);

      final var config = new Configuration(
          EngDataParams.defaults,
          SchedulingParams.defaults,
          MasterActivityDurations.defaults,
          CommParameters.defaults,
          new OrbiterParams(
              new OrbiterConfiguration("uhf", channelsExcludingV01, 0.0, true),
              OrbiterConfiguration.defaults,
              OrbiterConfiguration.defaults,
              OrbiterConfiguration.defaults,
              OrbiterConfiguration.defaults
          )
      );
      mission = new Mission(ctx.registrar(), Instant.EPOCH, config);
      ctx.use(mission);
      dataModel = mission.dataModel;
    }

    @Test
    public void testDownlinkVC00Delays() {
      final var vc00 = dataModel.getChannel(VC00);
      final var vc01 = dataModel.getChannel(VC01);
      final var vc02 = dataModel.getChannel(VC02);

      // Fill the vcs
      vc00.increaseDataRate(20);
      vc01.increaseDataRate(20);
      vc02.increaseDataRate(20);

      delay(1, Duration.SECOND);

      vc00.increaseDataRate(-20);
      vc01.increaseDataRate(-20);
      vc02.increaseDataRate(-20);

      spawn(mission, new UHFActive("ODY_TestID", 20, Duration.of(10, SECONDS)));

      // All channels should start with 20 MBits
      var expectedVC00data = 20.0;
      var expectedVC01data = 20.0;
      var expectedVC02data = 20.0;
      var expectedVC03data = 20.0;
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // First half of downlink should skip VC00
      delay(5, SECONDS);
      expectedVC00data += 5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE();
      expectedVC01data -= 10;
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // Second half of downlink should start with VC00
      delay(5, SECONDS);
      expectedVC00data += -10 + (5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE());
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // After pass data volumes should be unaffected
      delay(5, SECONDS);
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());
    }

    @Test
    public void testDownlinkVC00DelaysWithAlternateBlock() {
      final var vc00 = dataModel.getChannel(VC00);
      final var vc01 = dataModel.getChannel(VC01);
      final var vc02 = dataModel.getChannel(VC02);

      // Fill the vcs
      vc00.increaseDataRate(20);
      vc01.increaseDataRate(20);
      vc02.increaseDataRate(20);

      delay(1, Duration.SECOND);

      vc00.increaseDataRate(-20);
      vc01.increaseDataRate(-20);
      vc02.increaseDataRate(-20);

      mission.commModel.setAlternateUhfBlockInUse(CommModel.Orbiter.ODY, true);
      spawn(mission, new UHFActive("ODY_TestID", 20, Duration.of(10, SECONDS)));

      // All channels should start with 20 MBits
      var expectedVC00data = 20.0;
      var expectedVC01data = 20.0;
      var expectedVC02data = 20.0;
      var expectedVC03data = 20.0;
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // First half of downlink should skip VC00
      delay(5, SECONDS);
      expectedVC00data += 5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE();
      expectedVC02data -= 10;
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // Second half of downlink should start with VC00
      delay(5, SECONDS);
      expectedVC00data += -10 + (5 * mission.config.engDataParams().UHF_ACTIVE_DATA_RATE());
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());

      // After pass data volumes should be unaffected
      delay(5, SECONDS);
      assertWithinTolerance(expectedVC00data, vc00.volume.get());
      assertWithinTolerance(expectedVC01data, vc01.volume.get());
      assertWithinTolerance(expectedVC02data, vc02.volume.get());
    }
  }

  private static void assertWithinTolerance(final double expected, final double actual) {
    assertEquals(expected, actual, 0.0000001);
  }
}
