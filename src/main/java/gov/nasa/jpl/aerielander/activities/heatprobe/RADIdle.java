package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.RADState.Idle;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_28V_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRSTD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADCALHTRSTD_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRHR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRSTD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RADHTRSTD_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_RAD_EXT;

@ActivityType("RADIdle")
public final class RADIdle {
  @Parameter
  public Duration duration = Duration.of(1, MINUTE);

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;
    final var powerModel = mission.powerModel;

    HeatProbeModel.setRADState(Idle);

    powerModel.setPelState(HeatProbe_RAD_EXT, "off");
    powerModel.setPelState(HeatProbe_RAD_BEE, "off");
    powerModel.setPelState(HeatProbe_28V_EXT, "off");
    powerModel.setPelState(HeatProbe_28V_BEE, "off");
    powerModel.setPelState(HeatProbe_RADHTRSTD_EXT, "off");
    powerModel.setPelState(HeatProbe_RADHTRSTD_BEE, "off");
    powerModel.setPelState(HeatProbe_RADHTRHR_EXT, "off");
    powerModel.setPelState(HeatProbe_RADHTRHR_BEE, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRSTD_EXT, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRSTD_BEE, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_EXT, "off");
    powerModel.setPelState(HeatProbe_RADCALHTRHR_BEE, "off");

    delay(duration);
  }
}
