package gov.nasa.jpl.aerielander.models.power;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.StringValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import java.util.HashMap;
import java.util.Map;

public final class PowerModel {

  private final Register<Double> genericPowerUsed = Register.forImmutable(0.0); // Watts
  private final Map<PelItem, Register<String>> pelStates;

  public PowerModel() {
    pelStates = new HashMap<>(PelItem.values().length);
    for (final var item : PelItem.values()) pelStates.put(item, Register.forImmutable(item.defaultValue));
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete(String.format("%s/genericPowerUsed", basePath), genericPowerUsed, new DoubleValueMapper());
    for (final var entry : pelStates.entrySet()) {
      registrar.discrete(String.format("%s/pelStates/%s", basePath, entry.getKey().toString()), entry.getValue(), new StringValueMapper());
    }
  }

  public void setGenericPowerUsed(final double value) {
    genericPowerUsed.set(value);
  }

  public double getGenericPowerUsed() {
    return genericPowerUsed.get();
  }

  public void setPelState(final PelItem pelItem, final String state) {
    pelStates.get(pelItem).set(state);
  }

  public String getPelState(final PelItem item) {
    return pelStates.get(item).get();
  }

  public enum PelItem {
    PDDU("off"),
    CDH("off"),
    Harness("off"),
    BATT1_MEQ("off"),
    BATT2_MEQ("off"),
    USM_EFF("off"),
    SABC_DIODE("off"),
    ENG_GENERIC("off"),
    MIMU_A("off"),
    SA_Depl_Heater_py("off"),
    SA_Depl_Heater_my("off"),
    UHF_Lander("off"),
    UHF_RF("off"),
    SA_Depl_Motor_py("off"),
    SEIS_TSB_CHAHTR_EXT("off"),
    SEIS_TSB_CHAHTR_EBOX("off"),
    IDA_AEEHTRS_EXT("off"),
    IDA_AEEHTRS_PEB("off"),
    SDST("off"),
    SEIS_IDLE_EXT("off"),
    SEIS_IDLE_EBOX("off"),
    SEIS_SP1_EXT("off"),
    SEIS_SP1_EBOX("off"),
    SEIS_SP2_EXT("off"),
    SEIS_SP2_EBOX("off"),
    SEIS_SP3_EXT("off"),
    SEIS_SP3_EBOX("off"),
    SEIS_VBB1_EXT("off"),
    SEIS_VBB1_EBOX("off"),
    SEIS_VBB2_EXT("off"),
    SEIS_VBB2_EBOX("off"),
    SEIS_VBB3_EXT("off"),
    SEIS_VBB3_EBOX("off"),
    SEIS_CNTRMTR_EXT("off"),
    SEIS_CNTRMTR_EBOX("off"),
    SEIS_SCIT_EXT("off"),
    SEIS_SCIT_EBOX("off"),
    SEIS_MDE_EXT("off"),
    SEIS_MDE_EBOX("off"),
    SEIS_LVL_EXT("off"),
    SEIS_LVL_EBOX("off"),
    SEIS_MDEHTR_EXT("off"),
    SEIS_MDEHTR_EBOX("off"),
    SEIS_GEN_EXT("off"),
    SEIS_GEN_EBOX("off"),
    SEIS_TSB_CVRHTR_EXT("off"),
    SEIS_TSB_CVRHTR_EBOX("off"),
    IDA_WRSTHTR_EXT("off"),
    IDA_WRSTHTR_PEB("off"),
    IDA_IDLE_EXT("off"),
    IDA_IDLE_PEB("off"),
    IDA_MOVE_EXT("off"),
    IDA_MOVE_PEB("off"),
    IDA_GRAPPLE_EXT("off"),
    IDA_GRAPPLE_PEB("off"),
    IDA_GEN_EXT("off"),
    IDA_GEN_PEB("off"),
    IDC_IDLE_EXT("off"),
    IDC_IDLE_PEB("off"),
    IDC_IMAGE_EXT("off"),
    IDC_IMAGE_PEB("off"),
    SA_Depl_HOPS_py("off"),
    SA_Depl_HOPS_my("off"),
    Prop_pressure_xducer("off"),
    SA_Depl_Motor_my("off"),
    ICC_HTR_EXT("off"),
    ICC_HTR_PEB("off"),
    HeatProbe_IDLE_EXT("off"),
    HeatProbe_IDLE_BEE("off"),
    HeatProbe_28V_EXT("off"),
    HeatProbe_28V_BEE("off"),
    HeatProbe_2424DC_EXT("off"),
    HeatProbe_2424DC_BEE("off"),
    HeatProbe_RAD_EXT("off"),
    HeatProbe_RAD_BEE("off"),
    HeatProbe_RADHTRSTD_EXT("off"),
    HeatProbe_RADHTRSTD_BEE("off"),
    HeatProbe_RADHTRHR_EXT("off"),
    HeatProbe_RADHTRHR_BEE("off"),
    HeatProbe_RADCALHTRSTD_EXT("off"),
    HeatProbe_RADCALHTRSTD_BEE("off"),
    HeatProbe_RADCALHTRHR_EXT("off"),
    HeatProbe_RADCALHTRHR_BEE("off"),
    HeatProbe_TEMP_EXT("off"),
    HeatProbe_TEMP_BEE("off"),
    HeatProbe_TEMA_EXT("off"),
    HeatProbe_TEMA_BEE("off"),
    HeatProbe_TEMA_HTR_EXT("off"),
    HeatProbe_TEMA_HTR_BEE("off"),
    HeatProbe_HAMMER_EXT("off"),
    HeatProbe_HAMMER_BEE("off"),
    HeatProbe_MOTORHTR_EXT("off"),
    HeatProbe_MOTORHTR_BEE("off"),
    HeatProbe_TLM_EXT("off"),
    HeatProbe_TLM_BEE("off"),
    HeatProbe_STATIL_EXT("off"),
    HeatProbe_STATIL_BEE("off"),
    HeatProbe_TCHTR_EXT("off"),
    HeatProbe_TCHTR_BEE("off"),
    HeatProbe_TMHTR_EXT("off"),
    HeatProbe_TMHTR_BEE("off"),
    HeatProbe_GEN_EXT("off"),
    HeatProbe_GEN_BEE("off"),
    SSPA_Lander("off"),
    SSPA_RF("off"),
    ICC_IDLE_EXT("off"),
    ICC_IDLE_PEB("off"),
    ICC_IMAGE_EXT("off"),
    ICC_IMAGE_PEB("off"),
    IDC_HTR_EXT("off"),
    IDC_HTR_PEB("off"),
    APSS_IDLE_EXT("off"),
    APSS_IDLE_PAE("off"),
    APSS_28V_EXT("off"),
    APSS_28V_PAE("off"),
    APSS_TWINSPY_EXT("off"),
    APSS_TWINSPY_PAE("off"),
    APSS_HEATPY_EXT("off"),
    APSS_HEATPY_PAE("off"),
    APSS_TWINSMY_EXT("off"),
    APSS_TWINSMY_PAE("off"),
    APSS_HEATMY_EXT("off"),
    APSS_HEATMY_PAE("off"),
    APSS_IFG_EXT("off"),
    APSS_IFG_PAE("off"),
    APSS_PS_EXT("off"),
    APSS_PS_PAE("off"),
    APSS_GEN_EXT("off"),
    APSS_GEN_PAE("off"),
    IDA_Heater_Table("off"),
    APSS_Instrument("off"),
    APSS_Lander("off"),
    HeatProbe_Instrument("off"),
    HeatProbe_Lander("off"),
    ICC("off"),
    IDS_Instrument("off"),
    IDS_Lander("off"),
    SEIS_Heater("off"),
    SEIS_Instrument("off"),
    SEIS_Lander("off"),
    Payload_Avg_CBE("off"),
    Smart_Short("off"),
    IDA_AEEHTRS2_EXT("off"),
    IDA_AEEHTRS2_PEB("off"),
    IDA_WRSTHTR2_EXT("off"),
    IDA_WRSTHTR2_PEB("off");

    public final String defaultValue;

    PelItem(final String defaultValue) {
      this.defaultValue = defaultValue;
    }
  }
}
