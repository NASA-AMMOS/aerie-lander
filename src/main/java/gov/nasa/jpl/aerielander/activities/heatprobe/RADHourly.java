package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.RADState.Hourly;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_EXT;

@ActivityType("RADHourly")
public final class RADHourly {
  @Parameter
  public Duration duration = Duration.of(1, MINUTE);

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;
    final var powerModel= mission.powerModel;

    HeatProbeModel.setRADState(Hourly);

    powerModel.setPelState(HeatProbe_RAD_EXT, "on");
    powerModel.setPelState(HeatProbe_RAD_BEE, "on");
    powerModel.setPelState(HeatProbe_28V_EXT, "on");
    powerModel.setPelState(HeatProbe_28V_BEE, "on");
    powerModel.setPelState(HeatProbe_RADHTRHR_EXT, "on");
    powerModel.setPelState(HeatProbe_RADHTRHR_BEE, "on");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_EXT, "on");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_BEE, "on");

    delay(duration);
  }
}
