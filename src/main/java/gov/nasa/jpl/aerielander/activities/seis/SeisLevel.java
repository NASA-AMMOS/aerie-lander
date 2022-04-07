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
 * Effectively supports SEIS_LEVEL.
 * Turns the level motors PEL state on in the model for the activity duration, as well as setting the MDE to 'on'.
 * Generates the amount of data specified by the parameter.
 * Use this activity for both standard "level"ing but also "level low", since this model isn't high enough fidelity
 * to care which specific activity is being represented.
 */
@ActivityType("SeisLevel")
public final class SeisLevel {

  @Parameter
  public Duration duration = Duration.of(30, MINUTES);

  @Parameter
  public double dataVolume = 0.49; // Mbit

  @EffectModel
  public void run(final Mission mission) {
    final var pm = mission.powerModel;
    final var durSec = duration.ratioOver(SECONDS);
    final var dataRate = dataVolume / durSec;

    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, dataRate);
    pm.setPelState(PowerModel.PelItem.SEIS_LVL_EXT, "on");
    pm.setPelState(PowerModel.PelItem.SEIS_LVL_EBOX, "on");
    mission.seisModel.mdeShouldBeOn.set(true);
    mission.seisModel.runMDEStateMachine(pm);

    delay(duration);

    pm.setPelState(PowerModel.PelItem.SEIS_LVL_EXT, "off");
    pm.setPelState(PowerModel.PelItem.SEIS_LVL_EBOX, "off");
    mission.seisModel.mdeShouldBeOn.set(false);
    mission.seisModel.runMDEStateMachine(pm);
    mission.dataModel.increaseDataRate(DataConfig.APID.APID_SEIS_CONTINUOUS_SCI, -dataRate);
  }
}
