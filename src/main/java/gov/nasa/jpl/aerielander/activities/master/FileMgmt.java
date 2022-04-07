package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("FileMgmt")
public final class FileMgmt {
  @EffectModel
  public void run(final Mission mission) {
    // Nothing to do
    delay(mission.config.masterActivityDurations().FILE_MGMT_DURATION());
  }
}
