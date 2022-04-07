package gov.nasa.jpl.aerielander.models.seis;

import gov.nasa.jpl.aerie.contrib.models.Register;

import java.util.List;

public final class SeisConfig {

  public enum Gain {
    LOW("LG"),
    HIGH("HG");

    public final String abbrev;
    Gain(final String abbrev) { this.abbrev = abbrev; }
  }

  public enum Device {
    VBB1,
    VBB2,
    VBB3,
    SP1,
    SP2,
    SP3,
    SCIT
  }

  public enum DeviceType {
    VEL,
    POS,
    TEMP,
    SP,
    SCIT
  }

  public enum Channel {
    VBB1_VEL_LR_LG_EN,
    VBB1_VEL_LR_LG_SC,
    VBB1_VEL_LR_HG_EN,
    VBB1_VEL_LR_HG_SC,
    VBB1_VEL_HR_LG_EN,
    VBB1_VEL_HR_LG_SC,
    VBB1_VEL_HR_HG_EN,
    VBB1_VEL_HR_HG_SC,
    VBB1_POS_LR_LG_EN,
    VBB1_POS_LR_LG_SC,
    VBB1_POS_LR_HG_EN,
    VBB1_POS_LR_HG_SC,
    VBB1_POS_HR_LG_EN,
    VBB1_POS_HR_LG_SC,
    VBB1_POS_HR_HG_EN,
    VBB1_POS_HR_HG_SC,
    VBB1_TMP_LR,
    VBB1_TMP_HR,
    VBB2_VEL_LR_LG_EN,
    VBB2_VEL_LR_LG_SC,
    VBB2_VEL_LR_HG_EN,
    VBB2_VEL_LR_HG_SC,
    VBB2_VEL_HR_LG_EN,
    VBB2_VEL_HR_LG_SC,
    VBB2_VEL_HR_HG_EN,
    VBB2_VEL_HR_HG_SC,
    VBB2_POS_LR_LG_EN,
    VBB2_POS_LR_LG_SC,
    VBB2_POS_LR_HG_EN,
    VBB2_POS_LR_HG_SC,
    VBB2_POS_HR_LG_EN,
    VBB2_POS_HR_LG_SC,
    VBB2_POS_HR_HG_EN,
    VBB2_POS_HR_HG_SC,
    VBB2_TMP_LR,
    VBB2_TMP_HR,
    VBB3_VEL_LR_LG_EN,
    VBB3_VEL_LR_LG_SC,
    VBB3_VEL_LR_HG_EN,
    VBB3_VEL_LR_HG_SC,
    VBB3_VEL_HR_LG_EN,
    VBB3_VEL_HR_LG_SC,
    VBB3_VEL_HR_HG_EN,
    VBB3_VEL_HR_HG_SC,
    VBB3_POS_LR_LG_EN,
    VBB3_POS_LR_LG_SC,
    VBB3_POS_LR_HG_EN,
    VBB3_POS_LR_HG_SC,
    VBB3_POS_HR_LG_EN,
    VBB3_POS_HR_LG_SC,
    VBB3_POS_HR_HG_EN,
    VBB3_POS_HR_HG_SC,
    VBB3_TMP_LR,
    VBB3_TMP_HR,
    SP1_LR_LG,
    SP1_LR_HG,
    SP1_HR_LG,
    SP1_HR_HG,
    SP2_LR_LG,
    SP2_LR_HG,
    SP2_HR_LG,
    SP2_HR_HG,
    SP3_LR_LG,
    SP3_LR_HG,
    SP3_HR_LG,
    SP3_HR_HG,
    SCIT_HR,
    SCIT_LR
  }

  public enum VBBMode {
    SCI("SC"),
    ENG("EN");

    public final String abbrev;
    VBBMode(final String abbrev) { this.abbrev = abbrev; }
  }

  public static final record ChannelRate(Double inRate, Double outRate) { }

  public static final record ChannelOutRateGroup(double outRate, List<Channel> channels) { }

  public static final record DeviceTypeMetrics(Register<Double> samplingRate, Register<Gain> gain) {
    public DeviceTypeMetrics(final double samplingRate, final Gain gain) {
      this(Register.forImmutable(samplingRate), Register.forImmutable(gain));
    }

    public DeviceTypeMetrics() {
      this(0.0, Gain.HIGH);
    }
  }
}
