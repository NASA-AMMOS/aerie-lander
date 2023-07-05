package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.wake.WakeModel;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;

/**
 * Effectively supports SEIS_PWR_ON.
 */
@ActivityType("SeisPowerOn")
public final class SeisPowerOn {

  @Parameter
  public Duration duration = Duration.of(3, MINUTES);

  @Parameter
  public boolean vbbOn = true;

  @Parameter
  public boolean spOn = true;

  @Parameter
  public boolean scitOn = true;

  @EffectModel
  public void run(final Mission mission) {
    final var seisHk = mission.dataModel.hkModel.SEIS;
    final var seisNonChanHk = mission.dataModel.hkModel.SEIS_NON_CHAN;
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisHk, seisHk.defaultFullWakeRate, seisHk.defaultDiagnosticWakeRate);
    mission.dataModel.setInstrumentHKRate(WakeModel.WakeType.FULL, seisNonChanHk, seisNonChanHk.defaultFullWakeRate, seisNonChanHk.defaultDiagnosticWakeRate);

    call(mission, new SeisPowerSensorInfrastructure(
        duration,
        true,
        vbbOn ? VBBState.allOn() : VBBState.allOff(),
        spOn ? SPState.allOn() : SPState.allOff(),
        scitOn));
  }
}
