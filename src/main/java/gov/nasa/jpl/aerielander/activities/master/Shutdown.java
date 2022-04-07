package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECOND;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName.RETX;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.CDH;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.Harness;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.PDDU;
import static gov.nasa.jpl.aerielander.models.wake.WakeModel.WakeType.DIAGNOSTIC;

@ActivityType("Shutdown")
public final class Shutdown {
  @EffectModel
  public void run(final Mission mission) {
    final var dataModel = mission.dataModel;
    final var powerModel = mission.powerModel;
    final var duration = (mission.wakeModel.wakeType.get().equals(DIAGNOSTIC)) ?
        mission.config.masterActivityDurations().SHUTDOWN_DIAG_DURATION() :
        mission.config.masterActivityDurations().SHUTDOWN_FULL_DURATION();
    final var dumpDataRate = mission.config.engDataParams().SHUTDOWN_DATA_DUMP() / duration.ratioOver(SECOND);
    final var retransmitDataRate = dataModel.getChannel(RETX).volume.get() / duration.ratioOver(SECOND);

    dataModel.increaseDataRate(SPACECRAFT, dumpDataRate);
    dataModel.getChannel(RETX).increaseDataRate(-retransmitDataRate);

    delay(duration);

    dataModel.increaseDataRate(SPACECRAFT, -dumpDataRate);
    dataModel.getChannel(RETX).increaseDataRate(retransmitDataRate);

    powerModel.setPelState(CDH, "sleep");
    powerModel.setPelState(PDDU, "sleep");
    powerModel.setPelState(Harness, "sleep");
  }
}
