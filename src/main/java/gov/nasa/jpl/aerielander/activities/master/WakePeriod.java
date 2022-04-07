package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

@ActivityType("WakePeriod")
public final class WakePeriod {
  @EffectModel
  public void run(final Mission mission) {
    // Nothing to do
  }
}
