package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_EVENT_HIGH_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_EVENT_LOW_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_EVENT_MED_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_SEIS_EVENT_HIGH_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_SEIS_EVENT_LOW_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_SEIS_EVENT_MED_PRI;

/**
 * Effectively supports EVENT_PROC.
 */
@ActivityType("EventProc")
public final class EventProc {

  @Parameter
  public Duration duration = Duration.of(15, MINUTES);

  @Parameter
  public double seisLow = 0.0; // Expected high priority data volume for SEIS (Mbits)

  @Parameter
  public double seisMed = 0.0; // Expected medium priority data volume for SEIS (Mbits)

  @Parameter
  public double seisHigh = 0.0; // Expected low priority data volume for SEIS (Mbits)

  @Parameter
  public double apssLow = 0.0; // Expected high priority data volume for APSS (Mbits)

  @Parameter
  public double apssMed = 0.0; // Expected medium priority data volume for APSS (Mbits)

  @Parameter
  public double apssHigh = 0.0; // Expected low priority data volume for APSS (Mbits)

  @EffectModel
  public void run(final Mission mission) {
    final var durSec = duration.ratioOver(SECONDS);

    // Some of the data generated here is from APSS, but the input to this activity doesn't know which is which,
    // and they're routed to the same virtual channel, so we'll put all the data in SEIS_EVENT_*

    final var seisLowRate = seisLow / durSec;
    final var seisMedRate = seisMed / durSec;
    final var seisHighRate = seisHigh / durSec;
    final var apssLowRate = apssLow / durSec;
    final var apssMedRate = apssMed / durSec;
    final var apssHighRate = apssHigh / durSec;

    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_HIGH_PRI, seisHighRate);
    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_MED_PRI, seisMedRate);
    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_LOW_PRI, seisLowRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_HIGH_PRI, apssHighRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_MED_PRI, apssMedRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_LOW_PRI, apssLowRate);
    delay(this.duration);
    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_HIGH_PRI, -seisHighRate);
    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_MED_PRI, -seisMedRate);
    mission.dataModel.increaseDataRate(APID_SEIS_EVENT_LOW_PRI, -seisLowRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_HIGH_PRI, -apssHighRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_MED_PRI, -apssMedRate);
    mission.dataModel.increaseDataRate(APID_APSS_EVENT_LOW_PRI, -apssLowRate);
  }
}
