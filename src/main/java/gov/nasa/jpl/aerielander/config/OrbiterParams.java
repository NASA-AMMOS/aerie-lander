package gov.nasa.jpl.aerielander.config;

public record OrbiterParams(
   OrbiterConfiguration ODY,
   OrbiterConfiguration MRO,
   OrbiterConfiguration TGO,
   OrbiterConfiguration MVN,
   OrbiterConfiguration MEX
) {
  public static final OrbiterParams defaults =
      new OrbiterParams(
          OrbiterConfiguration.defaults,
          OrbiterConfiguration.defaults,
          OrbiterConfiguration.defaults,
          OrbiterConfiguration.defaults,
          OrbiterConfiguration.defaults
      );
}
