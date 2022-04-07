package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.power.PowerModel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Supplier;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.models.seis.SeisConfig.Device;

/**
 * Effectively supports SEIS_PWR_SENSOR_INFRASTRUCTURE.
 */
@ActivityType("SeisPowerSensorInfrastructure")
public final class SeisPowerSensorInfrastructure {

  @Parameter
  public Duration duration = Duration.of(2, MINUTES);

  @Parameter
  public boolean seisOn = true;

  @Parameter
  public VBBState vbbState = VBBState.allOn();

  @Parameter
  public SPState spState = SPState.allOn();

  @Parameter
  public boolean scitOn = true;

  public SeisPowerSensorInfrastructure() { }

  public SeisPowerSensorInfrastructure(
      final Duration duration,
      final boolean seisOn,
      final VBBState vbbState,
      final SPState spState,
      final boolean scitOn)
  {
    this.duration = duration;
    this.seisOn = seisOn;
    this.vbbState = vbbState;
    this.spState = spState;
    this.scitOn = scitOn;
  }

  private void setPelStates(
      final PowerModel powerModel,
      final List<PowerModel.PelItem> items,
      final Supplier<String> getState)
  {
    final var state = getState.get();
    items.forEach(i -> powerModel.setPelState(i, state));
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);

    mission.seisModel.poweredOn.set(true);
    mission.seisModel.setDeviceState(Device.VBB1, vbbState.vbb1on());
    mission.seisModel.setDeviceState(Device.VBB2, vbbState.vbb2on());
    mission.seisModel.setDeviceState(Device.VBB3, vbbState.vbb3on());
    mission.seisModel.setDeviceState(Device.SP1, spState.sp1on());
    mission.seisModel.setDeviceState(Device.SP2, spState.sp2on());
    mission.seisModel.setDeviceState(Device.SP3, spState.sp3on());
    mission.seisModel.setDeviceState(Device.SCIT, scitOn);

    final var pm = mission.powerModel;
    final var areAllVBBsOff = !vbbState.vbb1on() && !vbbState.vbb2on() && !vbbState.vbb3on();
    final var areAllSPsOff = !spState.sp1on() && !spState.sp2on() && !spState.sp3on();

    setPelStates(pm,
      List.of(
        PowerModel.PelItem.SEIS_IDLE_EBOX,
        PowerModel.PelItem.SEIS_IDLE_EXT),
      () -> seisOn ? "on" : "of");

    List.of(
        Pair.of(vbbState.vbb1on(),
          List.of(
              PowerModel.PelItem.SEIS_VBB1_EBOX,
              PowerModel.PelItem.SEIS_VBB1_EXT)),
        Pair.of(vbbState.vbb2on(),
          List.of(
              PowerModel.PelItem.SEIS_VBB2_EBOX,
              PowerModel.PelItem.SEIS_VBB2_EXT)),
        Pair.of(vbbState.vbb3on(),
          List.of(
              PowerModel.PelItem.SEIS_VBB3_EBOX,
              PowerModel.PelItem.SEIS_VBB3_EXT))
    ).forEach(p -> {
        setPelStates(pm,
          p.getRight(),
          () -> {
            if (p.getLeft() && areAllSPsOff) {
              return "vbbonly";
            }
            else if (p.getLeft()) {
              return "on";
            }
            else {
              return "off";
            }
        });
    });

    List.of(
        Pair.of(spState.sp1on(),
          List.of(
              PowerModel.PelItem.SEIS_SP1_EBOX,
              PowerModel.PelItem.SEIS_SP1_EXT)),
        Pair.of(spState.sp2on(),
          List.of(
              PowerModel.PelItem.SEIS_SP2_EBOX,
              PowerModel.PelItem.SEIS_SP2_EXT)),
        Pair.of(spState.sp3on(),
          List.of(
              PowerModel.PelItem.SEIS_SP3_EBOX,
              PowerModel.PelItem.SEIS_SP3_EXT))
    ).forEach(p -> {
        setPelStates(pm,
          p.getRight(),
          () -> {
            if (p.getLeft() && areAllVBBsOff) {
              return "sponly";
            }
            else if (p.getLeft()) {
              return "on";
            }
            else {
              return "off";
            }
        });
    });

    setPelStates(pm,
      List.of(
        PowerModel.PelItem.SEIS_SCIT_EBOX,
        PowerModel.PelItem.SEIS_SCIT_EXT),
      () -> scitOn ? "on" : "of");

    mission.seisModel.runMDEStateMachine(pm);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
