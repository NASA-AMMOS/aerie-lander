package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOUR;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;

@ActivityType("MasterFull")
public final class MasterFull {
  @Parameter
  public Duration duration = Duration.of(1, HOUR);

  @Parameter
  public String seqid = "";

  @Parameter
  public boolean lmeCurveSelNeeded = false;

  public MasterFull() {}

  public MasterFull(final Duration duration, final String seqid, final boolean lmeCurveSelNeeded) {
    this.duration = duration;
    this.seqid = seqid;
    this.lmeCurveSelNeeded = lmeCurveSelNeeded;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var submaster_seqid = "ns" + seqid.substring(2, 6) + "_" + seqid.substring(7, 9);

    call(mission, new LoadBlockLib());

    call(mission, new Wakeup());

    call(mission, new FileMgmt());

    if (lmeCurveSelNeeded) {
      call(mission, new LmeCurveSel());
    }

    call(mission, new Submaster(mission.config.masterActivityDurations().SUBMASTER_DIAG_DURATION(), submaster_seqid));

    call(mission, new FSWDiag());

    call(mission, new FileCopy());

    call(mission, new Shutdown());
  }
}
