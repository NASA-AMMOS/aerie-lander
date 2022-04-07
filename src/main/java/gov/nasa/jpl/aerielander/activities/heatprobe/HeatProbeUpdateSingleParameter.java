package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

@ActivityType("HeatProbeUpdateSingleParameter")
public final class HeatProbeUpdateSingleParameter {
  @Parameter
  public Duration duration = Duration.of(5, SECONDS);

  @Parameter
  public HeatProbeParameter parameter = HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION;

  @Parameter
  public Duration newParameterValue = Duration.ZERO;

  @EffectModel
  public void run(final Mission mission) {
    mission.HeatProbeModel.updateCurrentParameter(parameter, newParameterValue);
    delay(duration);
  }
}
