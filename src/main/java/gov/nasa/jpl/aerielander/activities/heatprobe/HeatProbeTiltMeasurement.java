package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_STATIL_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_STATIL_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_EXT;

@ActivityType("HeatProbeTiltMeasurement")
public final class HeatProbeTiltMeasurement {
  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(HeatProbe_STATIL_EXT, "on");
    powerModel.setPelState(HeatProbe_STATIL_BEE, "on");
    powerModel.setPelState(HeatProbe_TEMA_EXT, "on");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "on");
    powerModel.setPelState(HeatProbe_28V_EXT, "on");
    powerModel.setPelState(HeatProbe_28V_BEE, "on");

    // Looks like data from this doesn't matter, it must be too small

    delay(Duration.of(20, MINUTES));

    powerModel.setPelState(HeatProbe_STATIL_EXT, "off");
    powerModel.setPelState(HeatProbe_STATIL_BEE, "off");
    powerModel.setPelState(HeatProbe_TEMA_EXT, "off");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "off");
    powerModel.setPelState(HeatProbe_28V_EXT, "off");
    powerModel.setPelState(HeatProbe_28V_BEE, "off");
  }
}
