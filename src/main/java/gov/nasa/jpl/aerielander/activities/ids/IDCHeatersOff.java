package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDC_HTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDC_HTR_PEB;

@ActivityType("IDCHeatersOff")
public final class IDCHeatersOff {

  @Parameter
  public Duration duration = Duration.of(10, SECONDS);

  public IDCHeatersOff () {}

  public IDCHeatersOff(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var clock = mission.clocks;
    final var end = clock.getCurrentTime().plus(duration);
    final var powerModel = mission.powerModel;

    powerModel.setPelState(IDC_HTR_EXT, "off");
    powerModel.setPelState(IDC_HTR_PEB, "off");

    waitUntil(clock.whenTimeIsReached(end));
  }
}
