package gov.nasa.jpl.aerielander.activities.heatprobe;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOUR;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_GEN_BEE;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.HeatProbe_GEN_EXT;

@ActivityType("HeatProbeGeneric")
public final class HeatProbeGeneric {

  @Parameter
  public Duration duration = Duration.of(1, HOUR);

  @Parameter
  public int actID = 0;

  @Parameter
  public double HeatProbeSciData = 0.0; // Mbits

  @Parameter
  public double externalEnergy = 0.0; // Wh

  @Parameter
  public double beeEnergy = 0.0; // Wh

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var HeatProbeModel = mission.HeatProbeModel;

    final var durationInSeconds = duration.ratioOver(SECONDS);
    final var externalPower = externalEnergy*3600/durationInSeconds; // Watts
    final var beePower = beeEnergy*3600/durationInSeconds; // Watts
    final var dataRate = HeatProbeSciData/durationInSeconds;
    //mmpatSetParamData("HeatProbe_GEN_EXT", "pel_load_duty_factor_list", to_string(External_Power)) ;
    //mmpatSetParamData("HeatProbe_GEN_BEE", "pel_load_duty_factor_list", to_string(BEE_Power)) ;
    powerModel.setGenericPowerUsed(externalPower + beePower);
    powerModel.setPelState(HeatProbe_GEN_EXT, "on");
    powerModel.setPelState(HeatProbe_GEN_BEE, "on");

    HeatProbeModel.increaseDataRate(dataRate);
    delay(duration);
    HeatProbeModel.increaseDataRate(-dataRate);

    //mmpatSetParamData("HeatProbe_GEN_EXT", "pel_load_duty_factor_list", to_string(1.0)) ;
    //mmpatSetParamData("HeatProbe_GEN_BEE", "pel_load_duty_factor_list", to_string(1.0)) ;
    powerModel.setGenericPowerUsed(1.0);
    powerModel.setPelState(HeatProbe_GEN_EXT, "off");
    powerModel.setPelState(HeatProbe_GEN_BEE, "off");
  }
}
