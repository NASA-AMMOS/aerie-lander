@MissionModel(model = Mission.class)

@WithConfiguration(Configuration.class)

@WithMappers(BasicValueMappers.class)
@WithMappers(AerieLanderValueMappers.class)

@WithActivityType(Wake.class)
@WithActivityType(WakePeriod.class)
@WithActivityType(BootInit.class)
@WithActivityType(MasterFull.class)
@WithActivityType(MasterDiagnostic.class)
@WithActivityType(LoadBlockLib.class)
@WithActivityType(Wakeup.class)
@WithActivityType(FileMgmt.class)
@WithActivityType(LmeCurveSel.class)
@WithActivityType(Submaster.class)
@WithActivityType(FileCopy.class)
@WithActivityType(FSWDiag.class)
@WithActivityType(Shutdown.class)
@WithActivityType(Cleanup.class)
@WithActivityType(PayGetData.class)
@WithActivityType(GetData.class)
@WithActivityType(UHFComm.class)
@WithActivityType(UHF.class)
@WithActivityType(UHFPrep.class)
@WithActivityType(UHFActive.class)
@WithActivityType(XBandComm.class)
@WithActivityType(XBandCleanup.class)
@WithActivityType(XBandPrep.class)
@WithActivityType(XBandActive.class)
@WithActivityType(EngGeneric.class)
@WithActivityType(Retransmit.class)
@WithActivityType(EnterSafeMode.class)
@WithActivityType(ExitSafeMode.class)
@WithActivityType(ToggleTeHeaters.class)
@WithActivityType(TetherStorageBoxChassisHeaterOn.class)
@WithActivityType(TetherStorageBoxCoverHeaterOn.class)
@WithActivityType(TetherStorageBoxAllHeatersOff.class)
@WithActivityType(UseAlternateUhfBlock.class)
@WithActivityType(AddDataToVirtualChannel.class)
@WithActivityType(UpdateFpt.class)
@WithActivityType(UpdateDart.class)
@WithActivityType(ChangeDefaultFpt.class)
@WithActivityType(ChangeDefaultDart.class)
@WithActivityType(HeatProbeGeneric.class)
@WithActivityType(HeatProbeBeeOff.class)
@WithActivityType(HeatProbeBeeOn.class)
@WithActivityType(HeatProbeGetSciData.class)
@WithActivityType(HeatProbeCheckout.class)
@WithActivityType(HeatProbeHammeringCycle.class)
@WithActivityType(HeatProbeTiltMeasurement.class)
@WithActivityType(HeatProbeNewParameterTable.class)
@WithActivityType(HeatProbeUpdateSingleParameter.class)
@WithActivityType(HeatProbeTemP.class)
@WithActivityType(HeatProbeTemA.class)
@WithActivityType(HeatProbeTemPPlusTemA.class)
@WithActivityType(HeatProbeSingleTemA.class)
@WithActivityType(HeatProbePreheatTemA.class)
@WithActivityType(HeatProbeStatilTLMCheckout.class)
@WithActivityType(HeatProbeSinglePenetrationHammering.class)
@WithActivityType(RADIdle.class)
@WithActivityType(RADCalibration.class)
@WithActivityType(RADSingle.class)
@WithActivityType(RADStandard.class)
@WithActivityType(RADHourly.class)
@WithActivityType(SSAIdle.class)
@WithActivityType(SSAMonitoring.class)
@WithActivityType(IDSGeneric.class)
@WithActivityType(IDAMoveArm.class)
@WithActivityType(IDAGrapple.class)
@WithActivityType(IDAMovementPeriod.class)
@WithActivityType(IDCImages.class)
@WithActivityType(ICCImages.class)
@WithActivityType(IDCConditionalImage.class)
@WithActivityType(IDAHeatersOn.class)
@WithActivityType(IDAHeatersOff.class)
@WithActivityType(ICCHeatersOff.class)
@WithActivityType(ICCHeatersOn.class)
@WithActivityType(IDCHeatersOff.class)
@WithActivityType(IDCHeatersOn.class)
@WithActivityType(APSSChangeAcqConfig.class)
@WithActivityType(APSSProcessContinuousData.class)
@WithActivityType(APSSContinuousConfigFileUpdate.class)
@WithActivityType(APSSPaeDataRecovery.class)
@WithActivityType(APSSTwinsBoomSwap.class)
@WithActivityType(APSSGeneric.class)
@WithActivityType(SeisPowerOff.class)
@WithActivityType(EventProc.class)
@WithActivityType(SeisChangeAcqRate.class)
@WithActivityType(SeisChangeDataProcessConfig.class)
@WithActivityType(SeisChangeSPConfig.class)
@WithActivityType(SeisChangeVBBConfig.class)
@WithActivityType(SeisGeneric.class)
@WithActivityType(SeisGetTilt.class)
@WithActivityType(SeisLevel.class)
@WithActivityType(SeisPowerOffSensor.class)
@WithActivityType(SeisPowerOn.class)
@WithActivityType(SeisPowerOnSensor.class)
@WithActivityType(SeisPowerOnWithHeater.class)
@WithActivityType(SeisProcessContinuousData.class)
@WithActivityType(SeisRecenterRoutine.class)
@WithActivityType(SeisStartHeater.class)
@WithActivityType(SeisStopHeater.class)
@WithActivityType(SeisPowerSensorInfrastructure.class)
@WithActivityType(SeisPowerHeatersInfrastructure.class)
@WithActivityType(AllocateDSNStation.class)
@WithActivityType(SetDSNStationVisibility.class)
@WithActivityType(XBandCommSched.class)

package gov.nasa.jpl.aerielander;

import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
import gov.nasa.jpl.aerielander.activities.apss.APSSChangeAcqConfig;
import gov.nasa.jpl.aerielander.activities.apss.APSSContinuousConfigFileUpdate;
import gov.nasa.jpl.aerielander.activities.apss.APSSGeneric;
import gov.nasa.jpl.aerielander.activities.apss.APSSPaeDataRecovery;
import gov.nasa.jpl.aerielander.activities.apss.APSSProcessContinuousData;
import gov.nasa.jpl.aerielander.activities.apss.APSSTwinsBoomSwap;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHF;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHFActive;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHFComm;
import gov.nasa.jpl.aerielander.activities.comm.uhf.UHFPrep;
import gov.nasa.jpl.aerielander.activities.comm.xband.XBandActive;
import gov.nasa.jpl.aerielander.activities.comm.xband.XBandCleanup;
import gov.nasa.jpl.aerielander.activities.comm.xband.XBandComm;
import gov.nasa.jpl.aerielander.activities.comm.xband.XBandCommSched;
import gov.nasa.jpl.aerielander.activities.comm.xband.XBandPrep;
import gov.nasa.jpl.aerielander.activities.dsn.AllocateDSNStation;
import gov.nasa.jpl.aerielander.activities.dsn.SetDSNStationVisibility;
import gov.nasa.jpl.aerielander.activities.eng.AddDataToVirtualChannel;
import gov.nasa.jpl.aerielander.activities.eng.ChangeDefaultDart;
import gov.nasa.jpl.aerielander.activities.eng.ChangeDefaultFpt;
import gov.nasa.jpl.aerielander.activities.eng.EngGeneric;
import gov.nasa.jpl.aerielander.activities.eng.EnterSafeMode;
import gov.nasa.jpl.aerielander.activities.eng.ExitSafeMode;
import gov.nasa.jpl.aerielander.activities.eng.Retransmit;
import gov.nasa.jpl.aerielander.activities.eng.TetherStorageBoxAllHeatersOff;
import gov.nasa.jpl.aerielander.activities.eng.TetherStorageBoxChassisHeaterOn;
import gov.nasa.jpl.aerielander.activities.eng.TetherStorageBoxCoverHeaterOn;
import gov.nasa.jpl.aerielander.activities.eng.ToggleTeHeaters;
import gov.nasa.jpl.aerielander.activities.eng.UpdateDart;
import gov.nasa.jpl.aerielander.activities.eng.UpdateFpt;
import gov.nasa.jpl.aerielander.activities.eng.UseAlternateUhfBlock;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeBeeOff;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeBeeOn;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeCheckout;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeGeneric;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeGetSciData;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeHammeringCycle;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeNewParameterTable;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbePreheatTemA;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeSinglePenetrationHammering;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeSingleTemA;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeStatilTLMCheckout;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeTemA;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeTemP;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeTemPPlusTemA;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeTiltMeasurement;
import gov.nasa.jpl.aerielander.activities.heatprobe.HeatProbeUpdateSingleParameter;
import gov.nasa.jpl.aerielander.activities.heatprobe.RADCalibration;
import gov.nasa.jpl.aerielander.activities.heatprobe.RADHourly;
import gov.nasa.jpl.aerielander.activities.heatprobe.RADIdle;
import gov.nasa.jpl.aerielander.activities.heatprobe.RADSingle;
import gov.nasa.jpl.aerielander.activities.heatprobe.RADStandard;
import gov.nasa.jpl.aerielander.activities.heatprobe.SSAIdle;
import gov.nasa.jpl.aerielander.activities.heatprobe.SSAMonitoring;
import gov.nasa.jpl.aerielander.activities.ids.ICCHeatersOff;
import gov.nasa.jpl.aerielander.activities.ids.ICCHeatersOn;
import gov.nasa.jpl.aerielander.activities.ids.ICCImages;
import gov.nasa.jpl.aerielander.activities.ids.IDAGrapple;
import gov.nasa.jpl.aerielander.activities.ids.IDAHeatersOff;
import gov.nasa.jpl.aerielander.activities.ids.IDAHeatersOn;
import gov.nasa.jpl.aerielander.activities.ids.IDAMoveArm;
import gov.nasa.jpl.aerielander.activities.ids.IDAMovementPeriod;
import gov.nasa.jpl.aerielander.activities.ids.IDCConditionalImage;
import gov.nasa.jpl.aerielander.activities.ids.IDCHeatersOff;
import gov.nasa.jpl.aerielander.activities.ids.IDCHeatersOn;
import gov.nasa.jpl.aerielander.activities.ids.IDCImages;
import gov.nasa.jpl.aerielander.activities.ids.IDSGeneric;
import gov.nasa.jpl.aerielander.activities.master.BootInit;
import gov.nasa.jpl.aerielander.activities.master.Cleanup;
import gov.nasa.jpl.aerielander.activities.master.FSWDiag;
import gov.nasa.jpl.aerielander.activities.master.FileCopy;
import gov.nasa.jpl.aerielander.activities.master.FileMgmt;
import gov.nasa.jpl.aerielander.activities.master.GetData;
import gov.nasa.jpl.aerielander.activities.master.LmeCurveSel;
import gov.nasa.jpl.aerielander.activities.master.LoadBlockLib;
import gov.nasa.jpl.aerielander.activities.master.MasterDiagnostic;
import gov.nasa.jpl.aerielander.activities.master.MasterFull;
import gov.nasa.jpl.aerielander.activities.master.PayGetData;
import gov.nasa.jpl.aerielander.activities.master.Shutdown;
import gov.nasa.jpl.aerielander.activities.master.Submaster;
import gov.nasa.jpl.aerielander.activities.master.Wake;
import gov.nasa.jpl.aerielander.activities.master.WakePeriod;
import gov.nasa.jpl.aerielander.activities.master.Wakeup;
import gov.nasa.jpl.aerielander.activities.seis.EventProc;
import gov.nasa.jpl.aerielander.activities.seis.SeisChangeAcqRate;
import gov.nasa.jpl.aerielander.activities.seis.SeisChangeDataProcessConfig;
import gov.nasa.jpl.aerielander.activities.seis.SeisChangeSPConfig;
import gov.nasa.jpl.aerielander.activities.seis.SeisChangeVBBConfig;
import gov.nasa.jpl.aerielander.activities.seis.SeisGeneric;
import gov.nasa.jpl.aerielander.activities.seis.SeisGetTilt;
import gov.nasa.jpl.aerielander.activities.seis.SeisLevel;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerHeatersInfrastructure;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOff;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOffSensor;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOn;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOnSensor;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerOnWithHeater;
import gov.nasa.jpl.aerielander.activities.seis.SeisPowerSensorInfrastructure;
import gov.nasa.jpl.aerielander.activities.seis.SeisProcessContinuousData;
import gov.nasa.jpl.aerielander.activities.seis.SeisRecenterRoutine;
import gov.nasa.jpl.aerielander.activities.seis.SeisStartHeater;
import gov.nasa.jpl.aerielander.activities.seis.SeisStopHeater;
import gov.nasa.jpl.aerielander.config.Configuration;
import gov.nasa.jpl.aerielander.mappers.AerieLanderValueMappers;
