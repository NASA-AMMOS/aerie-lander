package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;

import java.util.Set;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.waitUntil;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

/**
 * Effectively supports SEIS_CHANGE_ACQ_RATE.
 */
@ActivityType("SeisChangeAcqRate")
public final class SeisChangeAcqRate {

  @Parameter
  public Duration duration = Duration.of(150, SECONDS); // 2.5 min.

  @Parameter
  public Double velRate = 0.0; // Hz

  @Parameter
  public Double posRate = 0.0; // Hz

  @Parameter
  public Double tempRate = 0.0; // Hz

  @Parameter
  public Double spRate = 0.0; // Hz

  @Parameter
  public Double scitRate = 0.0; // Hz

  @Validation("VEL rate must be either 0, 20 or 100 Hz")
  @Validation.Subject("velRate")
  public boolean validateVelRate() {
    return Set.of(0.0, 20.0, 100.0).contains(velRate);
  }

  @Validation("POS rate must be either 0, 0.1 or 1 Hz")
  @Validation.Subject("posRate")
  public boolean validatePosRate() {
    return Set.of(0.0, 0.1, 1.0).contains(posRate);
  }

  @Validation("TEMP rate must be either 0, 0.1 or 1 Hz")
  @Validation.Subject("tempRate")
  public boolean validateTempRate() {
    return Set.of(0.0, 0.1, 1.0).contains(tempRate);
  }

  @Validation("SP rate must be either 0, 20 or 100 Hz")
  @Validation.Subject("spRate")
  public boolean validateSpRate() {
    return Set.of(0.0, 20.0, 100.0).contains(spRate);
  }

  @Validation("SCIT rate must be either 0, 0.1 or 1 Hz")
  @Validation.Subject("scitRate")
  public boolean validateScitRate() {
    return Set.of(0.0, 0.1, 1.0).contains(scitRate);
  }

  @EffectModel
  public void run(final Mission mission) {
    final var end = mission.clocks.getCurrentTime().plus(duration);
    final var model = mission.seisModel;

    model.setSamplingRate(SeisConfig.DeviceType.VEL, velRate);
    model.setSamplingRate(SeisConfig.DeviceType.POS, posRate);
    model.setSamplingRate(SeisConfig.DeviceType.TEMP, tempRate);
    model.setSamplingRate(SeisConfig.DeviceType.SP, spRate);
    model.setSamplingRate(SeisConfig.DeviceType.SCIT, scitRate);

    waitUntil(mission.clocks.whenTimeIsReached(end));
  }
}
