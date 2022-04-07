package gov.nasa.jpl.aerielander.config;

import gov.nasa.jpl.aerielander.models.data.DataConfig;
import gov.nasa.jpl.aerielander.models.data.DataConfig.ChannelName;

import java.util.List;

public record OrbiterConfiguration(
   String blockName,
   List<ChannelName> vcsDownlinked,
   double dvAddedMbits,
   boolean delayForVC00
) {
  public static final OrbiterConfiguration defaults =
      new OrbiterConfiguration(
          "uhf",
          List.of(DataConfig.ChannelName.values()),
          0.0,
          false
      );
}
