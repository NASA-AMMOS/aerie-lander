package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

/**
 * Effectively supports SEIS_GET_TILT.
 * Performs a tilt measurement using SEIS.
 * Generates the amount of data specified by the parameter, and switches the MDE to "on" for its duration.
 */
@ActivityType("SeisGetTilt")
public final class SeisGetTilt {

  @Parameter
  public Duration duration = Duration.of(4, MINUTES);

  @Parameter
  public double dataVolume = 0.06; // Mbit

  @EffectModel
  public void run(final Mission mission) {
    final var durSec = duration.ratioOver(SECONDS);
    final var dataRate = dataVolume / durSec;

    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, dataRate);
    mission.seisModel.runMDEStateMachine(mission.powerModel);

    delay(duration);

    mission.seisModel.runMDEStateMachine(mission.powerModel);
    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, -dataRate);
  }
}
