package gov.nasa.jpl.aerielander.activities.comm.uhf;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.config.CommParameters;
import gov.nasa.jpl.aerielander.models.time.Time;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import static gov.nasa.jpl.aerielander.generated.ActivityActions.spawn;

@ActivityType("UHFComm")
public final class UHFComm {

  public enum IV_ENCODING {
    RS_I5,
    RS_OFF
  }

  public enum IV_DIAG_DATA {
    NO_DIAG_DATA,
    DIAG_DATA
  }

  @Parameter
  public String overflightID = "MRO_NSY_0001_00";

  @Parameter
  public Duration hailDuration = Duration.ZERO;

  @Parameter
  public double maxElevation = 0.0;

  @Parameter
  public double returnLinkVol = 0.0;

  @Parameter
  public Time LNUT = Time.fromUTC("2020-001T00:00:00");

  @Parameter
  public int forwardRate = 1; // kbps

  @Parameter
  public int returnRate = 1; // kbps

  @Parameter
  public Time firstBitTime = Time.fromUTC("2020-001T00:00:00");

  @Parameter
  public Time lastBitTime = Time.fromUTC("2020-001T00:00:00");

  @Parameter
  public IV_ENCODING iv_encoding = IV_ENCODING.RS_OFF;

  @Parameter
  public IV_DIAG_DATA iv_diag_data =  IV_DIAG_DATA.NO_DIAG_DATA;

  @Validation("LNUT must be after Mars Origin")
  public boolean validateLNUT() {
    return LNUT.isAfter(Time.Mars_Time_Origin);
  }

  @Validation("FirstBitTime must be after Mars Origin")
  public boolean validateFirstBitTime() {
    return firstBitTime.isAfter(Time.Mars_Time_Origin);
  }

  @Validation("LastBitTime must be after Mars Origin")
  public boolean validateLastBitTime() {
    return lastBitTime.isAfter(Time.Mars_Time_Origin);
  }

  @Validation("ForwardRate must be positive")
  public boolean validateForwardRate() {
    return forwardRate > 0;
  }

  @Validation("ReturnRate must be positive")
  public boolean validateReturnRate() {
    return returnRate > 0;
  }

  @EffectModel
  public void run(final Mission mission) {
    final var start = mission.clocks.getCurrentTime();
    final var commParameters = mission.config.commParameters();

    final var iv_sci_dur_2 =
        hailDuration
            .minus(commParameters.UHF_RT_3_DUR())
            .minus(commParameters.UHF_SCI_DUR_2_OFFSET());

    // In APGen, the Prep is scheduled in the past, since decomposition occurs before simulation
    // In Merlin, this cannot happen. We could do this with Scheduling at some point, but not yet
    // For now let's just have the start be the start of UHFComm. With default parameters this comes out
    // to 35 seconds later. Plans can be adjusted to move UHFComms 35 seconds earlier to account for this.
    //final var uhf_prep_start = getUhfBlockStart(start, commParameters);
    final var uhf_prep_start = start;
    final var uhf_prep_dur = commParameters.UHF_RT_DUR_1();
    final var uhf_active_start = uhf_prep_start.plus(uhf_prep_dur);
    final var total_uhf_dur = getUhfBlockEnd(start, hailDuration, commParameters).minus(uhf_prep_start);

    // the return volume depends on whether we're using RS encoding or not
    final double returnLinkEfficiency;
    if(iv_encoding == IV_ENCODING.RS_OFF){
      returnLinkEfficiency = commParameters.UHF_RETURN_LINK_UNENCODED_EFFICIENCY();
    } else {
      returnLinkEfficiency = commParameters.UHF_RETURN_LINK_ENCODED_EFFICIENCY();
    }

    final double info_Mbits = returnLinkVol *
                              returnLinkEfficiency *
                              commParameters.UHF_DATA_VOLUME_SCALAR() *
                              iv_sci_dur_2.dividedBy(hailDuration);

    delay(mission.clocks.timeUntil(uhf_prep_start));
    spawn(new UHF(total_uhf_dur, overflightID, info_Mbits));
    // UHF_SATF is for sequence generation, so we won't translate that
    //spawn(new UHFSatf(total_uhf_dur, overflightID, commParameters.UHF_RT_DUR_1(), iv_sci_dur_2, commParameters.UHF_RT_3_DUR(), iv_encoding, iv_diag_data));
    spawn(new UHFPrep(uhf_prep_dur, iv_diag_data));

    delay(mission.clocks.timeUntil(uhf_active_start));
    spawn(new UHFActive(overflightID, info_Mbits, iv_sci_dur_2));
  }

  private Time getUhfBlockStart(final Time requestStart, final CommParameters commParameters) {
    return requestStart
        .minus(commParameters.UHF_RT_DUR_1())
        .plus(commParameters.UHF_SCI_DUR_2_OFFSET());
  }

  private Time getUhfBlockEnd(
      final Time requestStart,
      final Duration requestDuration,
      final CommParameters commParameters
  ) {
    return requestStart.plus(requestDuration).plus(commParameters.UHF_CLEANUP_DUR());
  }
}
