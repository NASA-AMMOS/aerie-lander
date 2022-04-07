package gov.nasa.jpl.aerielander.activities.comm.xband;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.Harness;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SDST;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SSPA_Lander;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SSPA_RF;

@ActivityType("XBandCleanup")
public final class XBandCleanup {
  @Parameter public Duration duration = Duration.of(6, MINUTES);

  public XBandCleanup() {}

  public XBandCleanup(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    delay(duration);
    powerModel.setPelState(SDST, "off");
    powerModel.setPelState(SSPA_Lander, "off");
    powerModel.setPelState(SSPA_RF, "off");
    powerModel.setPelState(Harness, "wake");
  }
}
