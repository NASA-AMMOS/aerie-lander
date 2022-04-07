package gov.nasa.jpl.aerielander.activities.comm.uhf;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHFComm.IV_DIAG_DATA;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.SPACECRAFT;

@ActivityType("UHFPrep")
public final class UHFPrep {
  @Parameter
  public Duration duration = Duration.ZERO;

  @Parameter
  public IV_DIAG_DATA iv_diag_data = IV_DIAG_DATA.NO_DIAG_DATA;

  public UHFPrep() {}

  public UHFPrep(final Duration duration, final IV_DIAG_DATA iv_diag_data) {
    this.duration = duration;
    this.iv_diag_data = iv_diag_data;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var engDataParams = mission.config.engDataParams();
    final var dataModel = mission.dataModel;

    final double dataVol;
    if (this.iv_diag_data == IV_DIAG_DATA.DIAG_DATA) {
      dataVol = engDataParams.UHF_PREP_DATA_DUMP_DIAG();
    } else {
      dataVol = engDataParams.UHF_PREP_DATA_DUMP_NO_DIAG();
    }

    final var dataRate = dataVol / this.duration.in(Duration.SECONDS);
    dataModel.increaseDataRate(SPACECRAFT, dataRate);
    delay(this.duration);
    dataModel.increaseDataRate(SPACECRAFT, -dataRate);
  }
}
