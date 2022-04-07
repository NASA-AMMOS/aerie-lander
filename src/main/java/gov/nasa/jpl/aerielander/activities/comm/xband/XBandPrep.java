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

@ActivityType("XBandPrep")
public final class XBandPrep {
  @Parameter public Duration duration = Duration.of(6, MINUTES);

  public XBandPrep() {}

  public XBandPrep(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(SDST, "receive");
    powerModel.setPelState(Harness, "xband_receive");

    delay(duration);
  }
}
