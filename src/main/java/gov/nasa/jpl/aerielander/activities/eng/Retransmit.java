package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.RETX;

/**
 * Effectively supports Retransmit.
 */
@ActivityType("Retransmit")
public final class Retransmit
{

  @Parameter
  public Duration duration = Duration.of(5, SECONDS);

  @Parameter
  public double dataVolume = 0.0; // Mbits

  public Retransmit() { }

  public Retransmit(final double dataVolume) {
    this.dataVolume = dataVolume;
  }

  public Retransmit(final Duration duration, final double dataVolume) {
    this(dataVolume);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var vc = mission.dataModel.getChannel(RETX);
    final var dataRate = dataVolume / this.duration.in(SECONDS);
    vc.increaseDataRate(dataRate);
    delay(this.duration);
    vc.increaseDataRate(-dataRate);
  }
}
