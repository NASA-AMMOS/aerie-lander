package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_IDA_SCI_HI_PRI;
import static gov.nasa.jpl.aerielander.models.ids.IDSModel.IDAMode.Idle;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_MOVE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.IDA_MOVE_PEB;

@ActivityType("IDAMoveArm")
public final class IDAMoveArm {
   private final double IDA_MOVEMENT_DATA_RATE = 0.0032; // Mbits/s, and this includes estimation of data dumps

  @Parameter
  public Duration duration = Duration.of(20, MINUTES);

  public IDAMoveArm() {}

  public IDAMoveArm(final Duration duration) {
    this.duration = duration;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;
    final var idsModel = mission.idsModel;

    powerModel.setPelState(IDA_MOVE_EXT, "on");
    powerModel.setPelState(IDA_MOVE_PEB, "on");
    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, IDA_MOVEMENT_DATA_RATE);

    delay(duration);

    dataModel.increaseDataRate(APID_IDA_SCI_HI_PRI, -IDA_MOVEMENT_DATA_RATE);
    idsModel.setIDAMode(Idle);

    powerModel.setPelState(IDA_MOVE_EXT, "off");
    powerModel.setPelState(IDA_MOVE_PEB, "off");
  }
}
