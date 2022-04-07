package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS2_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS2_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_AEEHTRS_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR2_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR2_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_WRSTHTR_PEB;

@ActivityType("LoadBlockLib")
public final class LoadBlockLib {
  @EffectModel
  public void run(final Mission mission) {
    final var duration = mission.config.masterActivityDurations().LOAD_BLOCK_LIB_DURATION();
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;
    final var idsModel = mission.idsModel;

    if (idsModel.areIDASurvivalHeatersNominal()) {
      powerModel.setPelState(IDA_AEEHTRS_EXT, "on");
      powerModel.setPelState(IDA_AEEHTRS_PEB, "on");
      powerModel.setPelState(IDA_AEEHTRS2_EXT, "on");
      powerModel.setPelState(IDA_AEEHTRS2_PEB, "on");

      powerModel.setPelState(IDA_WRSTHTR_EXT, "on");
      powerModel.setPelState(IDA_WRSTHTR_PEB, "on");
      powerModel.setPelState(IDA_WRSTHTR2_EXT, "on");
      powerModel.setPelState(IDA_WRSTHTR2_PEB, "on");
    } else {
      powerModel.setPelState(IDA_AEEHTRS_EXT, "off");
      powerModel.setPelState(IDA_AEEHTRS_PEB, "off");
      powerModel.setPelState(IDA_AEEHTRS2_EXT, "off");
      powerModel.setPelState(IDA_AEEHTRS2_PEB, "off");

      powerModel.setPelState(IDA_WRSTHTR_EXT, "off");
      powerModel.setPelState(IDA_WRSTHTR_PEB, "off");
      powerModel.setPelState(IDA_WRSTHTR2_EXT, "off");
      powerModel.setPelState(IDA_WRSTHTR2_PEB, "off");
    }

    final var engDataRate = mission.config.engDataParams().AWAKE_ENG_DATA_RATE();
    dataModel.increaseDataRate(SPACECRAFT, engDataRate);
    delay(duration);
    dataModel.increaseDataRate(SPACECRAFT, -engDataRate);
  }
}
