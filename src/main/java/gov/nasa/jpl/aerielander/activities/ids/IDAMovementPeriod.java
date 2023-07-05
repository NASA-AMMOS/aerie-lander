package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.defer;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IDLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IDLE_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_IDLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_IDLE_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDC_IDLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDC_IDLE_PEB;

@ActivityType("IDAMovementPeriod")
public final class IDAMovementPeriod {
  @Parameter
  public Duration duration = Duration.of(12, MINUTES); // This includes move, grapple, and idle duration

  @Parameter
  public Duration moveDuration = Duration.ZERO;

  @Parameter
  public Duration grappleDuration = Duration.ZERO;

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;
    final var wakeModel = mission.wakeModel;
    final var hkModel = dataModel.hkModel;

    // Spawn children if necessary
    if (moveDuration.longerThan(Duration.ZERO)) spawn(mission, new IDAMoveArm(moveDuration));
    if (grappleDuration.longerThan(Duration.ZERO)) defer(moveDuration, mission, new IDAGrapple(grappleDuration));

    // No scheduling logic for now
    //if (move_duration > 00:00:00) {
    //  set CLEANUP_needed["IDA"](true) immediately;
    //}

    if (!(moveDuration.isEqualTo(Duration.ZERO) && grappleDuration.isEqualTo(Duration.ZERO))) {
      powerModel.setPelState(IDA_IDLE_EXT, "on");
      powerModel.setPelState(IDA_IDLE_PEB, "on");
    }

    /* No way to check number of instances of a given type right now, and this is just power stuff anyway
       which we are not actually doing right now
    if (length_of(all_instances_of_specified_type_within_window("ICC_IMAGES",start,start+span)) > 0){
      set PelSt["ICC_IDLE_EXT"]("on");
      set PelSt["ICC_IDLE_PEB"]("on");
    }
    if (length_of(all_instances_of_specified_type_within_window("IDC_IMAGES",start,start+span)) > 0){
      set PelSt["IDC_IDLE_EXT"]("on");
      set PelSt["IDC_IDLE_PEB"]("on");
    }*/

    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.IDA, hkModel.IDA.defaultFullWakeRate, hkModel.IDA.defaultDiagnosticWakeRate);
    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.IDC, hkModel.IDC.defaultFullWakeRate, hkModel.IDC.defaultDiagnosticWakeRate);

    delay(duration);

    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.IDA, 0, 0);
    dataModel.setInstrumentHKRate(wakeModel.wakeType.get(), hkModel.IDC, 0, 0);

    // we're turning off the IDC and IDA together because we're taking worst-case and not modeling fine timing
    powerModel.setPelState(ICC_IDLE_EXT, "off");
    powerModel.setPelState(ICC_IDLE_PEB, "off");
    powerModel.setPelState(IDC_IDLE_EXT, "off");
    powerModel.setPelState(IDC_IDLE_PEB, "off");
    powerModel.setPelState(IDA_IDLE_EXT, "off");
    powerModel.setPelState(IDA_IDLE_PEB, "off");
  }
}
