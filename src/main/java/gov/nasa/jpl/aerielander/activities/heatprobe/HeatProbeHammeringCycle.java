package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Idle;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Single;

@ActivityType("HeatProbeHammeringCycle")
public final class HeatProbeHammeringCycle {
  @Parameter
  public Duration timeout = Duration.of(77, HOURS);

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;

    HeatProbeModel.setSSAState(Single);

    // There is some other logic here, but it revolves around scheduling, so we aren't translating it
    delay(HeatProbeModel.getCurrentParameter(HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT));
    delay(HeatProbeModel.getCurrentParameter(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION));
    delay(HeatProbeModel.getCurrentParameter(HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION));

    HeatProbeModel.setSSAState(Idle);
  }
}
