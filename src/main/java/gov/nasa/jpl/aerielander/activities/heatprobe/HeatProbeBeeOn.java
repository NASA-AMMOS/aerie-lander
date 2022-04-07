package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.RADState;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Single;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_IDLE_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_IDLE_EXT;

@ActivityType("HeatProbeBeeOn")
public final class HeatProbeBeeOn {

  @Parameter
  public Duration duration = Duration.MINUTE;

  public HeatProbeBeeOn() { }

  public HeatProbeBeeOn(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var clock = mission.clocks;
    final var start = clock.getCurrentTime();
    final var end = start.plus(duration);
    final var powerModel = mission.powerModel;
    final var HeatProbeModel = mission.HeatProbeModel;
    final var dataModel = mission.dataModel;
    final var wakeModel = mission.wakeModel;
    final var hkModel = mission.dataModel.hkModel;

    powerModel.setPelState(HeatProbe_IDLE_EXT, "on");
    powerModel.setPelState(HeatProbe_IDLE_BEE, "on");
    HeatProbeModel.setRADState(RADState.Idle);
    HeatProbeModel.setSSAState(SSAState.Idle);
    HeatProbeModel.setParametersToTableValues();

    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.HeatProbe, hkModel.HeatProbe.defaultFullWakeRate, hkModel.HeatProbe.defaultDiagnosticWakeRate);
    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.HeatProbe_NON_CHAN, hkModel.HeatProbe_NON_CHAN.defaultFullWakeRate, hkModel.HeatProbe_NON_CHAN.defaultDiagnosticWakeRate);

    if (HeatProbeModel.getSSAState().equals(Single)) {
      HeatProbeModel.setDataRate(0.085);
    } else {
      HeatProbeModel.setDataRate(0.035);
    }

    HeatProbeModel.powerOn();
    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
