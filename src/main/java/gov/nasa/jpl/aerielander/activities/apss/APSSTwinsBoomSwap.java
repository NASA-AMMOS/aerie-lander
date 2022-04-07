package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;

/**
 * Effectively supports APSS_TWINS_BOOM_SWAP.
 * If -Y is off, turn it on and +Y off, and vice versa.
 * If both are already on or off, a constraint is thrown, and the activity does nothing.
 */
@ActivityType("APSSTwinsBoomSwap")
public final class APSSTwinsBoomSwap {

  @Parameter
  public Duration duration = Duration.of(20, MINUTES);

  public APSSTwinsBoomSwap() { }

  public APSSTwinsBoomSwap(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    final var twinsMyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_MY);
    final var twinsPyOn = mission.apssModel.getComponentState(APSSModel.Component.TWINS_PY);

    final var twinsMyPelItems = List.of(
      PowerModel.PelItem.APSS_TWINSPY_EXT,
      PowerModel.PelItem.APSS_TWINSPY_PAE,
      PowerModel.PelItem.APSS_HEATPY_EXT,
      PowerModel.PelItem.APSS_HEATPY_PAE);

    final var twinsPyPelItems = List.of(
      PowerModel.PelItem.APSS_TWINSMY_EXT,
      PowerModel.PelItem.APSS_TWINSMY_PAE,
      PowerModel.PelItem.APSS_HEATMY_EXT,
      PowerModel.PelItem.APSS_HEATMY_PAE);

    // If not exactly one boom is on, we exit without doing anything and trigger a constraint
    if (twinsMyOn.get() && twinsPyOn.get()) {
      // If +Y is off, turn it on
      twinsPyOn.set(true);
      twinsPyPelItems.forEach(i -> mission.powerModel.setPelState(i, "on"));
      // Turn -Y off
      twinsMyOn.set(false);
      twinsMyPelItems.forEach(i -> mission.powerModel.setPelState(i, "off"));
    }
    else if (!twinsMyOn.get() && twinsPyOn.get()) {
      // If -Y is off, turn it on
      twinsMyOn.set(true);
      twinsMyPelItems.forEach(i -> mission.powerModel.setPelState(i, "on"));
      // Turn +Y off
      twinsPyOn.set(false);
      twinsPyPelItems.forEach(i -> mission.powerModel.setPelState(i, "off"));
    }

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
