package gov.nasa.jpl.aerielander.activities.seis;

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
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_SEIS_CONTINUOUS_SCI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_SEIS_REC_HK;

/**
 * Effectively supports SEIS_GENERIC.
 */
@ActivityType("SeisGeneric")
public final class SeisGeneric {

  @Parameter
  public Duration duration = Duration.of(5, MINUTES);

  @Parameter
  public double continuousSciVolume = 0.0; // Mbits

  @Parameter
  public double recHKVolume = 0.0; // Mbits

  @Parameter
  public double externalEnergy = 0.0; // Wh

  @Parameter
  public double eboxEnergy = 0.0; // Wh

  @EffectModel
  public void run(final Mission mission) {
    final var durSec = this.duration.ratioOver(SECONDS);

    final var externalWatts = (externalEnergy*60*60)/durSec;
    final var eboxWatts = (eboxEnergy*60*60)/durSec;
    mission.powerModel.setGenericPowerUsed(externalWatts + eboxWatts);

    final var pelItems = List.of(PowerModel.PelItem.SEIS_GEN_EXT, PowerModel.PelItem.SEIS_GEN_EBOX);
    pelItems.forEach(i -> mission.powerModel.setPelState(i, "on"));
    mission.powerModel.setPelState(PowerModel.PelItem.ENG_GENERIC, "on");
//    mmpatSetParamData("SEIS_GENERIC", "pel_load_duty_factor_list", to_string(ENG_POWER));

    final var contSciRate = continuousSciVolume / durSec;
    final var recHKRate = recHKVolume / durSec;
    mission.dataModel.increaseDataRate(APID_SEIS_CONTINUOUS_SCI, contSciRate);
    mission.dataModel.increaseDataRate(APID_SEIS_REC_HK, recHKRate);
    delay(this.duration);
    mission.dataModel.increaseDataRate(APID_SEIS_REC_HK, -recHKRate);
    mission.dataModel.increaseDataRate(APID_SEIS_CONTINUOUS_SCI, -contSciRate);

//    mmpatSetParamData("SEIS_GENERIC", "pel_load_duty_factor_list", to_string(1.0));
    mission.powerModel.setGenericPowerUsed(1.0);
    pelItems.forEach(i -> mission.powerModel.setPelState(i, "off"));
  }
}
