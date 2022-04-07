package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_MOTORHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_STATIL_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_STATIL_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TLM_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TLM_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TMHTR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_TMHTR_EXT;

@ActivityType("HeatProbeStatilTLMCheckout")
public final class HeatProbeStatilTLMCheckout {
  @Parameter
  public Duration duration = Duration.of(2, HOURS);

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(HeatProbe_STATIL_EXT, "on");
    powerModel.setPelState(HeatProbe_STATIL_BEE, "on");
    powerModel.setPelState(HeatProbe_TLM_EXT, "on");
    powerModel.setPelState(HeatProbe_TLM_BEE, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "on");
    powerModel.setPelState(HeatProbe_TMHTR_EXT, "on");
    powerModel.setPelState(HeatProbe_TMHTR_BEE, "on");
    powerModel.setPelState(HeatProbe_28V_EXT, "on");
    powerModel.setPelState(HeatProbe_28V_BEE, "on");

    delay(duration);

    powerModel.setPelState(HeatProbe_STATIL_EXT, "off");
    powerModel.setPelState(HeatProbe_STATIL_BEE, "off");
    powerModel.setPelState(HeatProbe_TLM_EXT, "off");
    powerModel.setPelState(HeatProbe_TLM_BEE, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_MOTORHTR_BEE, "off");
    powerModel.setPelState(HeatProbe_TMHTR_EXT, "off");
    powerModel.setPelState(HeatProbe_TMHTR_BEE, "off");
    powerModel.setPelState(HeatProbe_28V_EXT, "off");
    powerModel.setPelState(HeatProbe_28V_BEE, "off");
  }
}
