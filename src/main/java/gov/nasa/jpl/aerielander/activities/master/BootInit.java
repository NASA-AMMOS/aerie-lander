package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.CDH;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.Harness;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.PDDU;

@ActivityType("BootInit")
public final class BootInit {
  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    powerModel.setPelState(CDH, "wake");
    powerModel.setPelState(PDDU, "wake");
    powerModel.setPelState(Harness, "wake");
  }
}
