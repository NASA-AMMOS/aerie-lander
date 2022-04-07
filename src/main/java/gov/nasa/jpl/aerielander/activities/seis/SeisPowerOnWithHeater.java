package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.wake.WakeModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

/**
 * Effectively supports SEIS_PWR_ON_WITH_HEATER.
 */
@ActivityType("SeisPowerOnWithHeater")
public final class SeisPowerOnWithHeater {

  @Parameter
  public Duration duration = Duration.of(90, SECONDS);

  @Parameter
  public boolean vbbOn = true;

  @Parameter
  public boolean spOn = true;

  @Parameter
  public boolean scitOn = true;

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    spawn(new SeisPowerSensorInfrastructure(duration,
                                            true,
                                            vbbOn ? VBBState.allOn() : VBBState.allOff(),
                                            spOn ? SPState.allOn() : SPState.allOff(),
                                            scitOn));
    spawn(new SeisPowerHeatersInfrastructure(true));

    final var seisHk = mission.dataModel.hkModel.SEIS;
    final var seisNonChanHk = mission.dataModel.hkModel.SEIS_NON_CHAN;
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisHk, seisHk.defaultFullWakeRate, seisHk.defaultDiagnosticWakeRate);
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisNonChanHk, seisNonChanHk.defaultFullWakeRate, seisNonChanHk.defaultDiagnosticWakeRate);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
