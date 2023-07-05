package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_IDC_1;

@ActivityType("IDCConditionalImage")
public final class IDCConditionalImage {
  @Parameter
  public Duration duration = Duration.of(6, MINUTES);

  @EffectModel
  public void run(final Mission mission) {
    call(mission, new IDCImages(duration, 0, APID_IDC_1, 95));
  }
}
