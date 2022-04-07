package gov.nasa.jpl.aerielander.activities.comm.xband;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.Harness;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SDST;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SSPA_Lander;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.SSPA_RF;

@ActivityType("XBandActive")
public final class XBandActive {
  @Parameter public Duration duration = Duration.of(6, MINUTES);

  public XBandActive() {}

  public XBandActive(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(SDST, "transmit");
    powerModel.setPelState(SSPA_Lander, "transmit");
    powerModel.setPelState(SSPA_RF, "transmit");
    powerModel.setPelState(Harness, "xband_transmit");

    delay(duration);
  }
}
