package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS2_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS2_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR2_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR2_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR_PEB;

@ActivityType("IDAHeatersOff")
public final class IDAHeatersOff {
  @Parameter
  public Duration duration = Duration.of(3, MINUTES);

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;

    powerModel.setPelState(IDA_AEEHTRS_EXT, "off");
    powerModel.setPelState(IDA_AEEHTRS_PEB, "off");
    powerModel.setPelState(IDA_AEEHTRS2_EXT, "off");
    powerModel.setPelState(IDA_AEEHTRS2_PEB, "off");
    powerModel.setPelState(IDA_WRSTHTR_EXT, "off");
    powerModel.setPelState(IDA_WRSTHTR_PEB, "off");
    powerModel.setPelState(IDA_WRSTHTR2_EXT, "off");
    powerModel.setPelState(IDA_WRSTHTR2_PEB, "off");

    delay(duration);
  }
}
