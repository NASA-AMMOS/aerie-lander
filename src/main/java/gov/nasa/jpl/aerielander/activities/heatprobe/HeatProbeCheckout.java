package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Checkout;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.SSAState.Idle;

@ActivityType("HeatProbeCheckout")
public final class HeatProbeCheckout {
  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;

    HeatProbeModel.setSSAState(Checkout);
    // For now we will manually recreate the scheduled plan, so we don't set these resources
    // set HeatProbe_activity_needed("TEMP") immediately;
    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_CO_TEMP_DURATION));
    //set HeatProbe_activity_needed("TEMA") immediately;
    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_CO_TEMA_DURATION));
    //set HeatProbe_activity_needed("CHECKOUT") immediately;
    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_CO_STATIL_TLM_DURATION));
    //set HeatProbe_activity_needed("TEMP") immediately;
    delay(HeatProbeModel.getCurrentParameter(PARAM_HeatProbe_CO_TEMP_DURATION));
    //set HeatProbe_activity_needed("None") immediately;
    HeatProbeModel.setSSAState(Idle);
  }
}
