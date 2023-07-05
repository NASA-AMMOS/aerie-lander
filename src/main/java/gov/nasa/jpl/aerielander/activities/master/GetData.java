package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeGetSciData;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.defer;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;


@ActivityType("GetData")
public final class GetData {
  @Parameter
  public Duration seisTimeout = Duration.of(10, MINUTES);

  @Parameter
  public Duration apssTimeout = Duration.of(10, MINUTES);

  @Parameter
  public Duration eventDuration = Duration.ZERO;

  @Parameter
  public Duration HeatProbeTimeout = Duration.of(5, MINUTES);

  @Parameter
  public Duration HeatProbeOffset = Duration.of(10, SECONDS);

  @EffectModel
  public void run(final Mission mission) {
    if (seisTimeout.longerThan(Duration.ZERO) ||
        apssTimeout.longerThan(Duration.ZERO) ||
        eventDuration.longerThan(Duration.ZERO)
    ) {
      spawn(mission, new PayGetData(seisTimeout, apssTimeout, eventDuration));
    }

    defer(HeatProbeOffset, mission, new HeatProbeGetSciData(HeatProbeTimeout));
  }
}
