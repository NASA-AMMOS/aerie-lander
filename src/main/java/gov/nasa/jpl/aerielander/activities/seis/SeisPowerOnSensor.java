package gov.nasa.jpl.aerielander.activities.seis;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.call;

/**
 * Effectively supports SEIS_PWR_ON_SENSOR.
 */
@ActivityType("SeisPowerOnSensor")
public final class SeisPowerOnSensor {

  @Parameter
  public Duration duration = Duration.of(2, MINUTES);

  @Parameter
  public VBBState vbbState = VBBState.allOn();

  @Parameter
  public SPState spState = SPState.allOn();

  @Parameter
  public boolean scitOn = true;

  @EffectModel
  public void run(final Mission mission) {
    call(new SeisPowerSensorInfrastructure(duration, true, vbbState, spState, scitOn));
  }
}
