package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_IDA_SCI_HI_PRI;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_IDA_SCI_LO_PRI;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_GEN_EXT;

@ActivityType("IDSGeneric")
public final class IDSGeneric {
  @Parameter
  public Duration duration = Duration.of(3, MINUTES);

  @Parameter
  public double decisionalDataVol = 0.0; // Mbits

  @Parameter
  public double nonDecisionalDataVol = 0.0; // Mbits

  @Parameter
  public double externalEnergy = 0.0; // Wh

  @EffectModel
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;

    final var durationInSeconds = duration.ratioOver(SECONDS);
    final var externalPower = externalEnergy*3600/durationInSeconds; // Watts
    final var decisionalDataRate = decisionalDataVol/durationInSeconds;
    final var nonDecisionalDataRate = nonDecisionalDataVol/durationInSeconds;
    //mmpatSetParamData("IDA_GEN_EXT", "pel_load_duty_factor_list", to_string(External_Power)) ;

    powerModel.setGenericPowerUsed(externalPower);
    powerModel.setPelState(IDA_GEN_EXT, "on");
    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, decisionalDataRate);
    dataModel.increaseDataRate(APID_IDA_SCI_LO_PRI, nonDecisionalDataRate);

    delay(duration);

    dataModel.increaseDataRate(APID_IDA_SCI_LO_PRI, -decisionalDataRate);
    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, -nonDecisionalDataRate);

    //mmpatSetParamData("IDA_GEN_EXT", "pel_load_duty_factor_list", to_string(1.0)) ;
    powerModel.setGenericPowerUsed(1.0);

    powerModel.setPelState(IDA_GEN_EXT, "off");
  }
}
