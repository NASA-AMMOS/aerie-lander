package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

/**
 * Effectively supports SEIS_RECENTER_ROUTINE.
 * Turns the centering motors PEL state on in the model for the activity duration.
 * Generates the amount of data specified by the parameter.
 * Use this for both "recenter routine" and just normal "recenter"ing, since our model
 * isn't high fidelity enough to care which specific one it is representing.
 */
@ActivityType("SeisRecenterRoutine")
public final class SeisRecenterRoutine {

  @Parameter
  public Duration duration = Duration.of(25, MINUTES);

  @Parameter
  public double dataVolume = 0.0; // Mbit

  @EffectModel
  public void run(final Mission mission) {
    final var pm = mission.powerModel;
    final var durSec = duration.ratioOver(SECONDS);
    final var dataRate = dataVolume / durSec;

    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, dataRate);
    pm.setPelState(PowerModel.PelItem.SEIS_CNTRMTR_EXT, "on");
    pm.setPelState(PowerModel.PelItem.SEIS_CNTRMTR_EBOX, "on");

    delay(duration);

    pm.setPelState(PowerModel.PelItem.SEIS_CNTRMTR_EXT, "off");
    pm.setPelState(PowerModel.PelItem.SEIS_CNTRMTR_EBOX, "off");
    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, -dataRate);
  }
}
