package gov.nasa.jpl.aerielander.activities.ids;

import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerielander.Mission;
import gov.nasa.jpl.aerielander.models.ids.IDSModel;

import java.util.List;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.MINUTES;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_1;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_2;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_3;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_4;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_5;
import static gov.nasa.jpl.aerielander.models.data.DataConfig.APID.APID_ICC_6;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IDLE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IDLE_PEB;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IMAGE_EXT;
import static gov.nasa.jpl.aerielander.models.power.PowerModel.PelItem.ICC_IMAGE_PEB;

@ActivityType("ICCImages")
public final class ICCImages {
  private static final List<APID> validAPIDs = List.of(APID_ICC_1,
                                                       APID_ICC_2,
                                                       APID_ICC_3,
                                                       APID_ICC_4,
                                                       APID_ICC_5,
                                                       APID_ICC_6);

  @Parameter
  public Duration duration = Duration.of(6, MINUTES);

  @Parameter
  public int nFrames = 1;

  @Parameter
  public APID apid = APID_ICC_6;

  @Parameter
  public int compQuality = 95;

  @Validation("nFrames must be between 0 and 99")
  @Validation.Subject("nFrames")
  public boolean validateNumberOfFrames() {
    return nFrames >= 0 && nFrames <= 99;
  }

  @Validation("apid must be an IDC APID")
  @Validation.Subject("apid")
  public boolean validateAPID() {
    return validAPIDs.contains(apid);
  }

  @Validation("compQuality must be between 0 and 99")
  @Validation.Subject("compQuality")
  public boolean validateCompQuality() {
    return compQuality >= 0 && compQuality <= 99;
  }

  @EffectModel
  @ControllableDuration(parameterName = "duration")
  public void run(final Mission mission) {
    final var powerModel = mission.powerModel;
    final var dataModel = mission.dataModel;

    // No scheduling logic for now
    //set CLEANUP_needed["ICC"](true) immediately;

    final var frameSize = IDSModel.computeSize(compQuality);
    final var dataRate = frameSize*nFrames/duration.ratioOver(SECONDS);

    // No image tracking yet
    //vc: string default to DWN_DART_ENTRY[APID].currentval();
    //track_image("ICC", Name, vc, FrameSize, Nframes, CompQuality, APID);

    powerModel.setPelState(ICC_IMAGE_EXT, "on");
    powerModel.setPelState(ICC_IMAGE_PEB, "on");
    powerModel.setPelState(ICC_IDLE_EXT, "on");
    powerModel.setPelState(ICC_IDLE_PEB, "on");
    dataModel.increaseDataRate(apid, dataRate);

    delay(duration);

    dataModel.increaseDataRate(apid, -dataRate);
    powerModel.setPelState(ICC_IMAGE_EXT, "off");
    powerModel.setPelState(ICC_IMAGE_PEB, "off");

    // Can't check if activity types are currently active at the moment, and this is just power anyway
    //if(IDA_MOVEMENT_PERIOD_CONSTRAINT_TRACKER.currentval() == 0){
    powerModel.setPelState(ICC_IDLE_EXT, "off");
    powerModel.setPelState(ICC_IDLE_PEB, "off");
    //}
  }
}
