package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;

import java.util.HashMap;
import java.util.Map;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;

/**
 * Effectively supports APSS_CONTINUOUS_CFG_FILE_UPDATE.
 * Use this activity every time you change the APSS flight software config file that controls continuous data processing.
 * Not doing so will lead to very incorrect data modeling results. Point to the csv summary of the file on DOM, NOT the actual FSW file itself.
 * Always put the same file for both "data_processing_csv_file_one_boom" and "data_processing_csv_file_two_booms" - you have to do this because
 * the config file has to be changed manually, not automatically during twins boom swaps as was thought at the time of writing the activity definition.
 */
@ActivityType("APSSContinuousConfigFileUpdate")
public final class APSSContinuousConfigFileUpdate {

  @Parameter
  public Duration duration = Duration.of(20, MINUTES);

  @Parameter
  public Map<APSSModel.Component, APSSModel.ComponentRate> componentInRates = new HashMap<>();

  @Parameter
  public Map<APSSModel.Component, APSSModel.ComponentRate> componentOutRates = new HashMap<>();

  @Parameter
  public double transferCoef = 0.2088; // Mbit/s

  public APSSContinuousConfigFileUpdate() { }

  public APSSContinuousConfigFileUpdate(
      final Duration duration,
      final Map<APSSModel.Component, APSSModel.ComponentRate> componentInRates,
      final Map<APSSModel.Component, APSSModel.ComponentRate> componentOutRates,
      final double transferCoef)
  {
    this.duration = duration;
    this.componentInRates = componentInRates;
    this.componentOutRates = componentOutRates;
    this.transferCoef = transferCoef;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var model = mission.apssModel;
    model.transferRate.set(transferCoef);

    // Set all defined in and out rates
    componentInRates.forEach((key, value) -> model.setComponentInRates(key, value.defaultRate(), value.bothBoomsOnRate()));
    componentOutRates.forEach((key, value) -> model.setComponentOutRates(key, value.defaultRate(), value.bothBoomsOnRate()));
  }
}
