package gov.nasa.jpl.aerielander.mappers;

import gov.nasa.jpl.aerie.merlin.framework.ValueMapper;
import gov.nasa.jpl.aerielander.activities.seis.SPState;
import gov.nasa.jpl.aerielander.activities.seis.VBBState;
import gov.nasa.jpl.aerielander.config.CommParameters;
import gov.nasa.jpl.aerielander.config.EngDataParams;
import gov.nasa.jpl.aerielander.config.MasterActivityDurations;
import gov.nasa.jpl.aerielander.config.OrbiterParams;
import gov.nasa.jpl.aerielander.config.SchedulingParams;
import gov.nasa.jpl.aerielander.mappers.apss.ComponentRateValueMapper;
import gov.nasa.jpl.aerielander.mappers.config.CommParametersMapper;
import gov.nasa.jpl.aerielander.mappers.config.EngDataParamsMapper;
import gov.nasa.jpl.aerielander.mappers.config.MasterActivityDurationsMapper;
import gov.nasa.jpl.aerielander.mappers.config.OrbiterParamsMapper;
import gov.nasa.jpl.aerielander.mappers.config.SchedulingParamsMapper;
import gov.nasa.jpl.aerielander.mappers.seis.ChannelRateValueMapper;
import gov.nasa.jpl.aerielander.mappers.seis.SPStateValueMapper;
import gov.nasa.jpl.aerielander.mappers.seis.VBBStateValueMapper;
import gov.nasa.jpl.aerielander.models.apss.APSSModel;
import gov.nasa.jpl.aerielander.models.seis.SeisConfig;
import gov.nasa.jpl.aerielander.models.time.Time;

public final class AerieLanderValueMappers {

    public static ValueMapper<Time> timeValueMapper() {
        return new TimeValueMapper();
    }

    public static ValueMapper<EngDataParams> engDataParamsMapper() {
        return new EngDataParamsMapper();
    }

    public static ValueMapper<SchedulingParams> schedulingParamsMapper() {
        return new SchedulingParamsMapper();
    }

    public static ValueMapper<MasterActivityDurations> masterActivityDurationsMapper() {
        return new MasterActivityDurationsMapper();
    }

    public static ValueMapper<CommParameters> commParametersMapper() {
        return new CommParametersMapper();
    }

    public static ValueMapper<OrbiterParams> orbiterParamsMapper() {
        return new OrbiterParamsMapper();
    }

    public static ValueMapper<APSSModel.ComponentRate> componentRate() {
        return new ComponentRateValueMapper();
    }

    public static ValueMapper<VBBState> vbbStateValueMapper() {
        return new VBBStateValueMapper();
    }

    public static ValueMapper<SPState> spStateValueMapper() {
        return new SPStateValueMapper();
    }

    public static ValueMapper<SeisConfig.ChannelRate> channelRateValueMapper() {
        return new ChannelRateValueMapper();
    }
}
