package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTE;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Idle;

@ActivityType("SSAIdle")
public final class SSAIdle {
  @Parameter
  public Duration duration = Duration.of(1, MINUTE);

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;

    HeatProbeModel.setSSAState(Idle);
    delay(duration);
  }
}
