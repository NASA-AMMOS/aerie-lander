package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.wake.WakeModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

/**
 * Effectively supports SEIS_PWR_OFF.
 */
@ActivityType("SeisPowerOff")
public final class SeisPowerOff {

  @Parameter
  public Duration duration = Duration.of(14, MINUTES);

  public SeisPowerOff() { }

  public SeisPowerOff(final Duration duration) { this.duration = duration; }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    spawn(new SeisPowerSensorInfrastructure(duration, false, VBBState.allOff(), SPState.allOff(), false));
    spawn(new SeisPowerHeatersInfrastructure(false));

    final var seisHk = mission.dataModel.hkModel.SEIS;
    final var seisNonChanHk = mission.dataModel.hkModel.SEIS_NON_CHAN;
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisHk, 0, 0);
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisNonChanHk, 0, 0);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
