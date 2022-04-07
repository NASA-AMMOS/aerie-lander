package gov.nasa.jpl.aerielander;

import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import gov.nasa.jpl.aerielander.models.comm.CommModel;
import gov.nasa.jpl.aerielander.models.data.DataModel;
import gov.nasa.jpl.aerielander.models.dsn.DSNModel;
import gov.nasa.jpl.aerielander.models.eng.EngModel;
import gov.nasa.jpl.aerielander.models.heatprobe.HeatProbeModel;
import gov.nasa.jpl.aerielander.models.ids.IDSModel;
import gov.nasa.jpl.aerielander.models.power.PowerModel;
import gov.nasa.jpl.aerielander.models.seis.SeisModel;
import gov.nasa.jpl.aerielander.models.time.Clocks;
import gov.nasa.jpl.aerielander.models.wake.WakeModel;

import static gov.nasa.jpl.aerielander.models.time.Time.Mars_Time_Origin;

public final class Mission {
  public final Configuration config;
  public final Clocks clocks;
  public final DataModel dataModel;
  public final DSNModel dsnModel;
  public final WakeModel wakeModel;
  public final CommModel commModel;
  public final PowerModel powerModel;
  public final EngModel engModel;
  public final HeatProbeModel HeatProbeModel;
  public final IDSModel idsModel;
  public final APSSModel apssModel;
  public final SeisModel seisModel;

  public Mission(final Registrar registrar, final Configuration config) {
    this.config = config;
    this.clocks = new Clocks(Mars_Time_Origin);
    this.dataModel = new DataModel();
    this.dsnModel = new DSNModel();
    this.wakeModel = new WakeModel();
    this.commModel = new CommModel();
    this.powerModel = new PowerModel();
    this.engModel = new EngModel();
    this.HeatProbeModel = new HeatProbeModel();
    this.idsModel = new IDSModel();
    this.apssModel = new APSSModel();
    this.seisModel = new SeisModel();

    // Register resources from models
    clocks.registerResources(registrar, "/clocks");
    dataModel.registerResources(registrar, "/data");
    dsnModel.registerResources(registrar,"/dsn");
    wakeModel.registerResources(registrar, "/wake");
    commModel.registerResources(registrar, "/comm");
    powerModel.registerResources(registrar, "/power");
    engModel.registerResources(registrar, "/eng");
    HeatProbeModel.registerResources(registrar, "/HeatProbe");
    idsModel.registerResources(registrar, "/ids");
    apssModel.registerResources(registrar, "/apss");
    seisModel.registerResources(registrar, "/seis");
  }
}
