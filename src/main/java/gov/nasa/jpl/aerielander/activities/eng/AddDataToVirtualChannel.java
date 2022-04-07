package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.data.DataConfig;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

/**
 * Effectively supports AddDataToVC.
 */
@ActivityType("AddDataToVirtualChannel")
public final class AddDataToVirtualChannel {

  @Parameter
  public Duration duration = Duration.MINUTE;

  // This initial value only exists since there must be an initial value
  @Parameter
  public DataConfig.ChannelName chanName = DataConfig.ChannelName.VC00;

  @Parameter
  public double dataVolume = 0.0; // Mbits

  public AddDataToVirtualChannel() { }

  public AddDataToVirtualChannel(final DataConfig.ChannelName chanName, final double dataVolume) {
    this.chanName = chanName;
    this.dataVolume = dataVolume;
  }

  public AddDataToVirtualChannel(final Duration duration, final DataConfig.ChannelName chanName, final double dataVolume) {
    this(chanName, dataVolume);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    final var durSec = this.duration.in(SECONDS);
    final var dataRate = dataVolume / durSec;
    final var vc = mission.dataModel.getChannel(chanName);

    vc.increaseDataRate(dataRate);
    delay(this.duration);
    vc.increaseDataRate(-dataRate);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
