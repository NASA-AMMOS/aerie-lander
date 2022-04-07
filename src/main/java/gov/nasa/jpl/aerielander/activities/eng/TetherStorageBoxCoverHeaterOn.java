package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports TetherStorageBoxCoverHeaterOn.
 */
@ActivityType("TetherStorageBoxCoverHeaterOn")
public final class TetherStorageBoxCoverHeaterOn {

  @Parameter
  public Duration duration = Duration.MINUTE;

  public TetherStorageBoxCoverHeaterOn() { }

  public TetherStorageBoxCoverHeaterOn(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    mission.powerModel.setPelState(PowerModel.PelItem.SEIS_TSB_CVRHTR_EBOX, "on");
    mission.powerModel.setPelState(PowerModel.PelItem.SEIS_TSB_CVRHTR_EXT, "on");
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
