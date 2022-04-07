package gov.nasa.jpl.aerielander.activities.dsn;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.dsn.DSNModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

@ActivityType("AllocateDSNStation")
public final class AllocateDSNStation {

  @Parameter
  public Duration duration = Duration.SECONDS;

  @Parameter
  public DSNModel.DSNStation dsnStation = DSNModel.DSNStation.Canberra;

  public AllocateDSNStation() { }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    mission.dsnModel.setAllocated(dsnStation, DSNModel.AllocatedEnum.Allocated);
    delay(duration);
    mission.dsnModel.setAllocated(dsnStation, DSNModel.AllocatedEnum.NotAllocated);
  }
}
