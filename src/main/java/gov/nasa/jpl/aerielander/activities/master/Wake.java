package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.time.Time;
import gov.nasa.jpl.aerielander.models.wake.WakeModel.WakeType;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOUR;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

@ActivityType("Wake")
public final class Wake {
  @Parameter
  public Duration duration = Duration.of(1, HOUR);

  @Parameter
  public WakeType wakeType = WakeType.DIAGNOSTIC; // Used by child activity MASTER to decompose appropriately

  @Parameter
  public String seqid = "";

  @EffectModel
  public void run(final Mission mission) {
    final var clock = mission.clocks;
    final var start = clock.getCurrentTime();
    final var end = start.plus(duration);
    final var wakeModel = mission.wakeModel;
    final var dataModel = mission.dataModel;
    final var hkModel = dataModel.hkModel;
    wakeModel.wakeType.set(wakeType);
    dataModel.setInstrumentHKRate(wakeType, hkModel.DUMP_CMD_HISTORY, hkModel.DUMP_CMD_HISTORY.defaultFullWakeRate, hkModel.DUMP_CMD_HISTORY.defaultDiagnosticWakeRate);
    dataModel.enableAllInstrumentHKRates(wakeType);

    spawn(mission, new WakePeriod());
    call(mission, new BootInit());

    final var masterDuration = duration.minus(mission.config.masterActivityDurations().BOOT_INIT_DURATION());
    if (wakeType.equals(WakeType.DIAGNOSTIC)) {
      call(mission, new MasterDiagnostic(masterDuration, seqid));
    } else {
      call(mission, new MasterFull(masterDuration, seqid, lmeCurveSelNeeded(start, end)));
    }

    dataModel.disableAllInstrumentHKRates(wakeType);
    wakeModel.wakeType.set(WakeType.NONE);
  }

  private boolean lmeCurveSelNeeded(final Time start, final Time end) {
    return start.solNumber() < end.solNumber();
  }
}
