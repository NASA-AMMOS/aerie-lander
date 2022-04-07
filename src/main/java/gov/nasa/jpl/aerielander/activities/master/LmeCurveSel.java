package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("LmeCurveSel")
public final class LmeCurveSel {
  @EffectModel
  public void run(final Mission mission) {
    // Nothing to do
    delay(mission.config.masterActivityDurations().LME_CURVE_SEL_DURATION());
  }
}
