package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

/**
 * Effectively supports SEIS_POWER_HEATERS_INFRASTRUCTURE.
 */
@ActivityType("SeisPowerHeatersInfrastructure")
public final class SeisPowerHeatersInfrastructure {

  @Parameter
  public Duration duration = Duration.SECOND;

  @Parameter
  public boolean heaterOn = true;

  public SeisPowerHeatersInfrastructure() { }

  public SeisPowerHeatersInfrastructure(final boolean heaterOn) { this.heaterOn = heaterOn; }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    List.of(
      PowerModel.PelItem.SEIS_MDEHTR_EXT,
      PowerModel.PelItem.SEIS_MDEHTR_EBOX)
        .forEach(i -> mission.powerModel.setPelState(i, heaterOn ? "on" : "off"));

    mission.seisModel.runMDEStateMachine(mission.powerModel);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
