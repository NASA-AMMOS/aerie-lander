package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.apss.APSSModel.LIMIT_RESOLUTION;

/**
 * Effectively supports APSS_PROCESS_CONT_DATA.
 */
@ActivityType("APSSProcessContinuousData")
public final class APSSProcessContinuousData {

  @Parameter
  public Duration timeout = Duration.of(20, MINUTES);

  public APSSProcessContinuousData() { }

  public APSSProcessContinuousData(final Duration timeout) {
    this.timeout = timeout;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var model = mission.apssModel;
    final var internalVolume = model.getInternalVolume();

    // If the transfer rate is low or the duration too short, we might not get all the data
    if (model.paePoweredOn.get() && internalVolume > LIMIT_RESOLUTION) {
      // Volume is in Mbits, rate is in Mbit/sec.
      final var internalTransferVolume = Math.min(internalVolume, timeout.ratioOver(SECONDS)*model.transferRate.get());
      final var dataTransferredRatio = internalTransferVolume/internalVolume;
      final var transferredVolume = model.getVolumeToSendToVC() * dataTransferredRatio;
      final var dataRate = transferredVolume / timeout.ratioOver(SECONDS);
      mission.dataModel.increaseDataRate(DataConfig.APID.APID_APSS_CONTINUOUS_SCI, dataRate);

      // Track the amount of data sent to VC for this particular activity
      model.addContinuousDataSentIn(transferredVolume);

      // Dump internal data invokes a delay
      model.dumpInternalData(timeout, internalTransferVolume, transferredVolume);

      model.addContinuousDataSentIn(-transferredVolume);
      mission.dataModel.increaseDataRate(DataConfig.APID.APID_APSS_CONTINUOUS_SCI, -dataRate);
    }
  }
}
