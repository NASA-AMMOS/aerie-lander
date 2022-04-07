package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_2424DC_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_2424DC_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TCHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TCHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_HTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_HTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TMHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TMHTR_EXT;

@ActivityType("HeatProbePreheatTemA")
public final class HeatProbePreheatTemA {
  @Parameter
  public Duration timeout = Duration.of(24, HOURS);

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(HeatProbe_TEMA_EXT, "on");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "on");
    powerModel.setPelState(HeatProbe_TEMA_HTR_EXT, "on");
    powerModel.setPelState(HeatProbe_TEMA_HTR_BEE, "on");
    powerModel.setPelState(HeatProbe_2424DC_EXT, "on");
    powerModel.setPelState(HeatProbe_2424DC_BEE, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "on");
    powerModel.setPelState(HeatProbe_TCHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_TCHTR_BEE, "on");
    powerModel.setPelState(HeatProbe_TMHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_TMHTR_BEE, "on");

    delay(timeout);

    powerModel.setPelState(HeatProbe_TEMA_EXT, "off");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "off");
    powerModel.setPelState(HeatProbe_TEMA_HTR_EXT, "off");
    powerModel.setPelState(HeatProbe_TEMA_HTR_BEE, "off");
    powerModel.setPelState(HeatProbe_2424DC_EXT, "off");
    powerModel.setPelState(HeatProbe_2424DC_BEE, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "off");
    powerModel.setPelState(HeatProbe_TCHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_TCHTR_BEE, "off");
    powerModel.setPelState(HeatProbe_TMHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_TMHTR_BEE, "off");
  }
}
