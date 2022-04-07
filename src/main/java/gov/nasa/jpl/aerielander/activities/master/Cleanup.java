package gov.nasa.jpl.aerielander.activities.master;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerielander.Mission;

@ActivityType("Cleanup")
public final class Cleanup {
  @Parameter
  public boolean clean_eng = false;

  @Parameter
  public boolean clean_icc = false;

  @Parameter
  public boolean clean_ida = false;

  @Parameter
  public boolean clean_idc = false;

  @Parameter
  public boolean clean_apss = false;

  @Parameter
  public boolean clean_seis = false;

  @Parameter
  public boolean clean_evnt = false;

  @Parameter
  public boolean clean_HeatProbe = false;

  @EffectModel
  public void run(final Mission mission) {
    // Nothing to do
  }
}
