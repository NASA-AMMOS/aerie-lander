package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_IDA_SCI_HI_PRI;
import static gov.nasa.jpl.aerielander.models.ids.IDSModel.IDAMode.Grappling;
import static gov.nasa.jpl.aerielander.models.ids.IDSModel.IDAMode.Idle;
import static gov.nasa.jpl.aerielander.models.ids.IDSModel.IDAMode.Moving;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_GRAPPLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_GRAPPLE_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_MOVE_EXT;

@ActivityType("IDAGrapple")
public final class IDAGrapple {
  private final double IDS_GRAPPLE_DATA_RATE = 0.192/3600.0; // Mbits/s

  @Parameter
  public Duration duration = Duration.of(20, MINUTES);

  public IDAGrapple() {}

  public IDAGrapple(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;
    final var idsModel = mission.idsModel;

    idsModel.setIDAMode(Grappling);

    powerModel.setPelState(IDA_GRAPPLE_EXT, "on");
    powerModel.setPelState(IDA_GRAPPLE_PEB, "on");
    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, IDS_GRAPPLE_DATA_RATE);

    delay(duration);

    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, -IDS_GRAPPLE_DATA_RATE);
    if (powerModel.getPelState(IDA_MOVE_EXT).equals("off")) {
      idsModel.setIDAMode(Idle);
    } else {
      idsModel.setIDAMode(Moving);
    }

    powerModel.setPelState(IDA_GRAPPLE_EXT, "off");
    powerModel.setPelState(IDA_GRAPPLE_PEB, "off");
  }
}
