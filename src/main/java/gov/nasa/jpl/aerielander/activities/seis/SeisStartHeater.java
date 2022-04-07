package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

/**
 * Effectively supports SEIS_START_HEATER.
 */
@ActivityType("SeisStartHeater")
public final class SeisStartHeater {

  @Parameter
  public Duration duration = Duration.MINUTE;

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    spawn(new SeisPowerHeatersInfrastructure(true));
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
