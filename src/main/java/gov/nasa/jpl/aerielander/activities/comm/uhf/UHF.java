package gov.nasa.jpl.aerielander.activities.comm.uhf;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;

@ActivityType("UHF")
public final class UHF {
  @Parameter
  public Duration duration = Duration.SECOND;

  @Parameter
  public String overflightID = "";

  @Parameter
  public double returnLinkVol = 0.0;

  public UHF() {}

  public UHF(final Duration duration, final String overflightID, final double info_Mbits) {
    this.duration = duration;
    this.overflightID = overflightID;
    this.returnLinkVol = info_Mbits;
  }

  @EffectModel
  public void run(final Mission mission) {
    // Nothing to do
  }
}
