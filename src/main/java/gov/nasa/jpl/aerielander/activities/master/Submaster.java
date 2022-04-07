package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;

@ActivityType("Submaster")
public final class Submaster {
  @Parameter
  public Duration duration = Duration.of(5, MINUTES);

  @Parameter
  public String seqid = "";

  public Submaster() {}

  public Submaster(final Duration duration, final String seqid) {
    this.duration = duration;
    this.seqid = seqid;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var dataModel = mission.dataModel;

    final var engDataRate = mission.config.engDataParams().AWAKE_ENG_DATA_RATE();
    dataModel.increaseDataRate(SPACECRAFT, engDataRate);
    delay(duration);
    dataModel.increaseDataRate(SPACECRAFT, -engDataRate);
  }
}
