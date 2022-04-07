package gov.nasa.jpl.aerielander.activities.apss;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_CONTINUOUS_SCI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_APSS_FSW_SPECIAL_EVR;

/**
 * Effectively supports APSS_GENERIC.
 * A generic APSS activity that uses the discrete amount of power and data specified by the parameters.
 * Use only when a more specific activity type does not exist for what you are trying to do.
 * Modifies no continuous states, so if you use it in place of an activity that actually modifies the
 * continuous state of the instrument, your modeling results will be wrong downstream.
 */
@ActivityType("APSSGeneric")
public final class APSSGeneric {
  private static final List<PowerModel.PelItem> pelItems = List.of(PowerModel.PelItem.APSS_GEN_EXT, PowerModel.PelItem.APSS_GEN_PAE);

  @Parameter
  public Duration duration = Duration.of(7, MINUTES);

  @Parameter
  public double continuousSciDataVolume = 0.0; // Mbits

  @Parameter
  public double fswSpecialEvrDataVolume = 0.0; // Mbits

  @Parameter
  public double externalEnergyUsed = 0.0; // Wh

  @Parameter
  public double eboxEnergyUsed = 0.0; // Wh

  public APSSGeneric() { }

  public APSSGeneric(
      final double continuousSciDataVolume,
      final double fswSpecialEvrDataVolume,
      final double externalEnergyUsed,
      final double eboxEnergyUsed)
  {
    this.continuousSciDataVolume = continuousSciDataVolume;
    this.fswSpecialEvrDataVolume = fswSpecialEvrDataVolume;
    this.externalEnergyUsed = externalEnergyUsed;
    this.eboxEnergyUsed = eboxEnergyUsed;
  }

  public APSSGeneric(
      final Duration duration,
      final double continuousSciDataVolume,
      final double fswSpecialEvrDataVolume,
      final double externalEnergyUsed,
      final double eboxEnergyUsed)
  {
    this(continuousSciDataVolume, fswSpecialEvrDataVolume, externalEnergyUsed, eboxEnergyUsed);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var durSec = this.duration.ratioOver(SECONDS);

    final var externalWatts = (externalEnergyUsed*60*60)/durSec;
    final var eboxWatts = (eboxEnergyUsed*60*60)/durSec;
//    mmpatSetParamData("APSS_GEN_EXT", "pel_load_duty_factor_list", to_string(External_Power)) ;
//    mmpatSetParamData("APSS_GEN_PAE", "pel_load_duty_factor_list", to_string(EBOX_Power)) ;
    mission.powerModel.setGenericPowerUsed(externalWatts + eboxWatts);
    pelItems.forEach(i -> mission.powerModel.setPelState(i, "on"));

    final var continuousSciDataRate = continuousSciDataVolume / durSec;
    final var fswSpecialEvrDataRate = fswSpecialEvrDataVolume / durSec;
    mission.dataModel.increaseDataRate(APID_APSS_CONTINUOUS_SCI, continuousSciDataRate);
    mission.dataModel.increaseDataRate(APID_APSS_FSW_SPECIAL_EVR, fswSpecialEvrDataRate);
    delay(this.duration);
    mission.dataModel.increaseDataRate(APID_APSS_CONTINUOUS_SCI, -continuousSciDataRate);
    mission.dataModel.increaseDataRate(APID_APSS_FSW_SPECIAL_EVR, -fswSpecialEvrDataRate);

//    mmpatSetParamData("APSS_GEN_EXT", "pel_load_duty_factor_list", to_string(1.0)) ;
//    mmpatSetParamData("APSS_GEN_PAE", "pel_load_duty_factor_list", to_string(1.0)) ;
    mission.powerModel.setGenericPowerUsed(1.0);
    pelItems.forEach(i -> mission.powerModel.setPelState(i, "off"));
  }
}
