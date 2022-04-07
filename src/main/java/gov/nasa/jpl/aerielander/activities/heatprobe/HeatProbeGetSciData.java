package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_HeatProbe_SCI;

@ActivityType("HeatProbeGetSciData")
public final class HeatProbeGetSciData {
  @Parameter
  public Duration timeout = Duration.of(10, MINUTES);

  public HeatProbeGetSciData() {}

  public HeatProbeGetSciData(final Duration timeout) {
    this.timeout = timeout;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;
    final var dataModel = mission.dataModel;

    final double currentVolume = HeatProbeModel.getDataVolume();

     // HeatProbe transfers to lander one packet per second, or 16.4 kbit/s, 0.0164 Mbit/s, or 59.04 Mbit/hr
    final double transferRate = 0.0164; // Mbit/second
    final Duration requiredDuration = Duration.roundNearest((currentVolume/transferRate), SECONDS);
    if (currentVolume > 0 && requiredDuration.isPositive()) {
      // Make sure the HeatProbe_get_scidata doesn't take longer than the timeout
      final Duration transferDuration = Duration.min(requiredDuration, timeout);
      final double transferVolume = transferRate * transferDuration.ratioOver(SECONDS);

      HeatProbeModel.increaseDataRate(-transferRate);
      dataModel.increaseDataRate(APID_HeatProbe_SCI, transferRate);
      HeatProbeModel.setSciDataSentInActivity(transferVolume);

      delay(transferDuration);

      HeatProbeModel.setSciDataSentInActivity(transferVolume);
      dataModel.increaseDataRate(APID_HeatProbe_SCI, -transferRate);
      HeatProbeModel.increaseDataRate(transferRate);
    }
  }
}
