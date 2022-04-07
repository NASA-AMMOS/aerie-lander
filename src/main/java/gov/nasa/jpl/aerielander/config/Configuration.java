package gov.nasa.jpl.aerielander.config;

import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;

public record Configuration(
        EngDataParams engDataParams,
        SchedulingParams schedulingParams,
        MasterActivityDurations masterActivityDurations,
        CommParameters commParameters,
        OrbiterParams orbiterParams) {

    @Export.Template
    public static
    Configuration defaultConfiguration() {
        return new Configuration(
                EngDataParams.defaults,
                SchedulingParams.defaults,
                MasterActivityDurations.defaults,
                CommParameters.defaults,
                OrbiterParams.defaults
        );
    }
}
