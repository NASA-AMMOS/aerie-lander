package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.activities.apss.APSSProcessContinuousData;
import gov.nasa.jpl.aerielander.activities.seis.SeisProcessContinuousData;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;

@ActivityType("PayGetData")
public final class PayGetData {
  @Parameter
  public Duration seisTimeout = Duration.of(10, MINUTES);

  @Parameter
  public Duration apssTimeout = Duration.of(10, MINUTES);

  @Parameter
  public Duration eventDuration = Duration.ZERO;

  public PayGetData() {}

  public PayGetData(final Duration seisTimeout, final Duration apssTimeout, final Duration eventDuration) {
    this.seisTimeout = seisTimeout;
    this.apssTimeout = apssTimeout;
    this.eventDuration = eventDuration;
  }

  @EffectModel
  public void run(final Mission mission) {
    if (seisTimeout.isPositive()) call(mission, new SeisProcessContinuousData(seisTimeout));
    if (apssTimeout.isPositive()) call(mission, new APSSProcessContinuousData(apssTimeout));

    // The apgen model really doesn't use eventDuration
  }
}
