package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Idle;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Single;

@ActivityType("HeatProbeTemPPlusTemA")
public final class HeatProbeTemPPlusTemA {
  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;
    HeatProbeModel.setSSAState(Single);

    // No scheduling logic for now
    //set HeatProbe_activity_needed("TEMP") immediately;

    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_SINGLEPEN_COOL_DURATION));

    // No scheduling logic for now
    //if(HeatProbe_activity_needed.currentval() != "None"){
    //  set HeatProbe_activity_needed("TEMA") immediately;

    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION));

    // No scheduling logic for now
    //set HeatProbe_activity_needed("None") immediately;

    HeatProbeModel.setSSAState(Idle);
  }
}
