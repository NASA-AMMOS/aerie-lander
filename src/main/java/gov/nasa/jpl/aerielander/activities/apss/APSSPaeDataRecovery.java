package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_CONTINUOUS_SCI;

/**
 * Effectively supports APSS_PAE_DATA_RECOVERY.
 * This activity should be used after safing APSS when turning it back on.
 * It dumps the input amount of data into APID_APSS_CONTINUOUS_SCI
 * and empties the stored APSS memory.
 */
@ActivityType("APSSPaeDataRecovery")
public final class APSSPaeDataRecovery {

  @Parameter
  public Duration duration = Duration.of(27, MINUTES);

  @Parameter
  public double dataVolume = 0.0; // MBits

  public APSSPaeDataRecovery() { }

  public APSSPaeDataRecovery(final Duration duration) {
    this.duration = duration;
  }

  public APSSPaeDataRecovery(final double dataVolume) {
    this.dataVolume = dataVolume;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var dataRate = dataVolume / duration.ratioOver(SECONDS);
    mission.dataModel.increaseDataRate(APID_APSS_CONTINUOUS_SCI, dataRate);
    // Dump internal data invokes a delay
    mission.apssModel.dumpInternalData(duration);
    mission.dataModel.increaseDataRate(APID_APSS_CONTINUOUS_SCI, -dataRate);
  }
}
