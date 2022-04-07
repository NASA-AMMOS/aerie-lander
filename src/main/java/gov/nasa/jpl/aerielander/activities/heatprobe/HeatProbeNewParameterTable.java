package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOUR;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_STATIL_TLM_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_TEMA_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_CO_TEMP_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_HAMMER_TIMEOUT;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_MON_TEMP_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_MON_WAIT_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_COOL_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_CAL_MEAS_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_HEATUP_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_HOURLY_WAIT_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_MEAS_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_SINGLEMEAS_DURATION;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_LONG;
import static gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel.HeatProbeParameter.PARAM_RAD_STD_WAIT_DURATION_SHORT;

@ActivityType("HeatProbeNewParameterTable")
public final class HeatProbeNewParameterTable {
  @Parameter
  public Duration duration = Duration.of(5, SECONDS);

  @Parameter
  public Duration param_HeatProbe_mon_temp_duration = Duration.of(5, MINUTES);

  @Parameter
  public Duration param_HeatProbe_mon_wait_duration = Duration.of(55, MINUTES);

  @Parameter
  public Duration param_HeatProbe_singlepen_cool_duration = Duration.of(3, HOURS);

  @Parameter
  public Duration param_HeatProbe_singlepen_tema_duration = Duration.of(1, HOUR);

  @Parameter
  public Duration param_HeatProbe_hammer_timeout = Duration.of(4, HOURS);

  @Parameter
  public Duration param_HeatProbe_co_temp_duration = Duration.of(10, MINUTES);

  @Parameter
  public Duration param_HeatProbe_co_tema_duration = Duration.of(12, MINUTES);

  @Parameter
  public Duration param_HeatProbe_co_statil_tlm_duration = Duration.of(14, MINUTES);

  @Parameter
  public Duration param_rad_heatup_duration = Duration.of(15, MINUTES);

  @Parameter
  public Duration param_rad_meas_duration = Duration.of(20, MINUTES);

  @Parameter
  public Duration param_rad_hourly_wait_duration = Duration.of(56, MINUTES).plus(Duration.of(4, SECONDS));

  @Parameter
  public Duration param_rad_std_wait_duration_short = Duration.of(2, HOURS).plus(Duration.of(4, MINUTES)).plus(Duration.of(58, SECONDS));

  @Parameter
  public Duration param_rad_std_wait_duration_long = Duration.of(8, HOURS).plus(Duration.of(14, MINUTES)).plus(Duration.of(52, SECONDS));

  @Parameter
  public Duration param_rad_singlemeas_duration = Duration.of(15, MINUTES);

  @Parameter
  public Duration param_rad_cal_meas_duration = Duration.of(10, MINUTES);

  @EffectModel
  public void run(final Mission mission) {
    final var HeatProbeModel = mission.HeatProbeModel;

    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_MON_TEMP_DURATION, param_HeatProbe_mon_temp_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_MON_WAIT_DURATION, param_HeatProbe_mon_wait_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_SINGLEPEN_COOL_DURATION, param_HeatProbe_singlepen_cool_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_SINGLEPEN_TEMA_DURATION, param_HeatProbe_singlepen_tema_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_HAMMER_TIMEOUT, param_HeatProbe_hammer_timeout);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_CO_TEMP_DURATION, param_HeatProbe_co_temp_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_CO_TEMA_DURATION, param_HeatProbe_co_tema_duration);
    HeatProbeModel.updateTableParameter(PARAM_HeatProbe_CO_STATIL_TLM_DURATION, param_HeatProbe_co_statil_tlm_duration);
    HeatProbeModel.updateTableParameter(PARAM_RAD_HEATUP_DURATION, param_rad_heatup_duration);
    HeatProbeModel.updateTableParameter(PARAM_RAD_MEAS_DURATION, param_rad_meas_duration);
    HeatProbeModel.updateTableParameter(PARAM_RAD_HOURLY_WAIT_DURATION, param_rad_hourly_wait_duration);
    HeatProbeModel.updateTableParameter(PARAM_RAD_STD_WAIT_DURATION_SHORT, param_rad_std_wait_duration_short);
    HeatProbeModel.updateTableParameter(PARAM_RAD_STD_WAIT_DURATION_LONG, param_rad_std_wait_duration_long);
    HeatProbeModel.updateTableParameter(PARAM_RAD_SINGLEMEAS_DURATION, param_rad_singlemeas_duration);
    HeatProbeModel.updateTableParameter(PARAM_RAD_CAL_MEAS_DURATION, param_rad_cal_meas_duration);

    HeatProbeModel.setParametersToTableValues();

    delay(duration);
  }
}
