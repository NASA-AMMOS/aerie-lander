package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECOND;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;
import static gov.nasa.jpl.aerielander.models.wake.WakeModel.WakeType.DIAGNOSTIC;

@ActivityType("Wakeup")
public final class Wakeup {
  @EffectModel
  public void run(final Mission mission) {
    final var engDataParams = mission.config.engDataParams();
    final var masterActivityDurations = mission.config.masterActivityDurations();
    final var dataModel = mission.dataModel;
    final var wakeType = mission.wakeModel.wakeType.get();
    final Duration duration;
    final double dumpData;
    if (wakeType.equals(DIAGNOSTIC)) {
      duration = masterActivityDurations.WAKEUP_DIAG_DURATION();
      dumpData = engDataParams.SHUTDOWN_DATA_DUMP();
    } else {
      duration = masterActivityDurations.WAKEUP_FULL_DURATION();
      dumpData = engDataParams.WAKEUP_FULL_DATA_DUMP();
    }

    final var engDataRate = engDataParams.AWAKE_ENG_DATA_RATE();
    dataModel.increaseDataRate(SPACECRAFT, engDataRate);

    //mmpatSetParamData("", "thrm_stat_disabled_list", "1 1 1 1");
    delay(duration.minus(Duration.of(1, SECOND)));

    dataModel.increaseDataRate(SPACECRAFT, dumpData);

    delay(Duration.of(1, SECOND));

    dataModel.increaseDataRate(SPACECRAFT, -dumpData);
    dataModel.increaseDataRate(SPACECRAFT, -engDataRate);

    dataModel.setDefaultDART();
    dataModel.setDefaultFPT();
  }
}
