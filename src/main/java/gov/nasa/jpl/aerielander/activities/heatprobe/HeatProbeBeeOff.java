package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.RADState;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_IDLE_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_IDLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRSTD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRSTD_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRSTD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRSTD_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_EXT;

@ActivityType("HeatProbeBeeOff")
public final class HeatProbeBeeOff {

  @Parameter
  public Duration duration = Duration.MINUTE;

  public HeatProbeBeeOff() { }

  public HeatProbeBeeOff(final Duration duration) {
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


    powerModel.setPelState(HeatProbe_RAD_EXT, "off");
    powerModel.setPelState(HeatProbe_RAD_BEE, "off");
    powerModel.setPelState(HeatProbe_28V_EXT, "off");
    powerModel.setPelState(HeatProbe_28V_BEE, "off");

    powerModel.setPelState(HeatProbe_RADHTRSTD_EXT, "off");
    powerModel.setPelState(HeatProbe_RADHTRSTD_BEE, "off");
    powerModel.setPelState(HeatProbe_RADHTRHR_EXT, "off");
    powerModel.setPelState(HeatProbe_RADHTRHR_BEE, "off");

    powerModel.setPelState(HeatProbe_RADCALHTRSTD_EXT, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRSTD_BEE, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_EXT, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_BEE, "off");

    // then turn off other HeatProbe states and the SSA state machine which does matter
    powerModel.setPelState(HeatProbe_IDLE_EXT, "off");
    powerModel.setPelState(HeatProbe_IDLE_BEE, "off");
    HeatProbeModel.setSSAState(SSAState.Off);

    // TODO: We are not doing scheduling at this time
    //use HeatProbe_SSA_begin(2000-1T00:01:00) immediately;

    HeatProbeModel.setRADState(RADState.Off);

    // TODO: We are not doing scheduling at this time
    //use HeatProbe_RAD_begin(2000-1T00:01:00) immediately;

    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.HeatProbe, 0, 0);
    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.HeatProbe_NON_CHAN, 0, 0);

    HeatProbeModel.setDataRate(0);
    HeatProbeModel.powerOff();

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
