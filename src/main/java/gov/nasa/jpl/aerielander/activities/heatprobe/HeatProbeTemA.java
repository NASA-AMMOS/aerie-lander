package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_2424DC_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_2424DC_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_HTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TEMA_HTR_EXT;

@ActivityType("HeatProbeTemA")
public final class HeatProbeTemA {
  @Parameter
  public Duration duration = Duration.of(1, MINUTE);

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(HeatProbe_TEMA_EXT, "on");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "on");
    powerModel.setPelState(HeatProbe_TEMA_HTR_EXT, "on");
    powerModel.setPelState(HeatProbe_TEMA_HTR_BEE, "on");
    powerModel.setPelState(HeatProbe_2424DC_EXT, "on");
    powerModel.setPelState(HeatProbe_2424DC_BEE, "on");

    delay(duration);

    powerModel.setPelState(HeatProbe_TEMA_EXT, "off");
    powerModel.setPelState(HeatProbe_TEMA_BEE, "off");
    powerModel.setPelState(HeatProbe_TEMA_HTR_EXT, "off");
    powerModel.setPelState(HeatProbe_TEMA_HTR_BEE, "off");
    powerModel.setPelState(HeatProbe_2424DC_EXT, "off");
    powerModel.setPelState(HeatProbe_2424DC_BEE, "off");
  }
}
