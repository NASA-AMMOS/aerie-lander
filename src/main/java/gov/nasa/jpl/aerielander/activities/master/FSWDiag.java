package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;

@ActivityType("FSWDiag")
public final class FSWDiag {
  @EffectModel
  public void run(final Mission model) {
    final var config = model.config;
    final var dataRate = config.engDataParams().AWAKE_ENG_DATA_RATE();

    model.dataModel.increaseDataRate(SPACECRAFT, dataRate);
    delay(config.masterActivityDurations().FSW_DIAG_DURATION());
    model.dataModel.increaseDataRate(SPACECRAFT, -dataRate);
  }
}
