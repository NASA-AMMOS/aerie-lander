package gov.nasa.jpl.aerielander.activities.eng;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.power.PowerModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;

/**
 * Effectively supports ENG_GENERIC.
 */
@ActivityType("EngGeneric")
public final class EngGeneric {

  @Parameter
  public Duration duration = Duration.of(4, SECONDS);

  @Parameter
  public double dataVolume = 0.0; // Mbits

  @Parameter
  public double energyUsed = 0.0; // Wh

  public EngGeneric() { }

  public EngGeneric(final double dataVolume, final double energyUsed) {
    this.dataVolume = dataVolume;
    this.energyUsed = energyUsed;
  }

  public EngGeneric(final Duration duration, final double dataVolume, final double energyUsed) {
    this(dataVolume, energyUsed);
    this.duration = duration;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var durSec = this.duration.in(SECONDS);
    final var watts = (energyUsed*60*60)/durSec;
//  mmpatSetParamData("ENG_GENERIC", "pel_load_duty_factor_list", to_string(ENG_POWER));
    mission.powerModel.setGenericPowerUsed(watts);
    mission.powerModel.setPelState(PowerModel.PelItem.ENG_GENERIC, "on");

    final var dataRate = dataVolume / durSec;
    mission.dataModel.increaseDataRate(SPACECRAFT, dataRate);
    delay(this.duration);
    mission.dataModel.increaseDataRate(SPACECRAFT, -dataRate);

//  mmpatSetParamData("ENG_GENERIC", "pel_load_duty_factor_list", to_string(1.0));
    mission.powerModel.setGenericPowerUsed(1.0);
    mission.powerModel.setPelState(PowerModel.PelItem.ENG_GENERIC, "off");
  }
}
