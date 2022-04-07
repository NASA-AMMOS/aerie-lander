package gov.nasa.jpl.aerielander.activities.comm.xband;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.comm.CommModel.XBandAntenna;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECOND;
import static gov.nasa.jpl.aerielander.models.comm.CommModel.XBandAntenna.EAST_MGA;

@ActivityType("XBandCommSched")
public final class XBandCommSched {
  @Parameter public Duration duration = Duration.of(1, SECOND);
  @Parameter public XBandAntenna xbandAntSel = EAST_MGA;
  @Parameter public String DSNTrack = "DSS-25";

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    mission.commModel.setXBandAntenna(xbandAntSel);
    delay(duration);
  }
}
