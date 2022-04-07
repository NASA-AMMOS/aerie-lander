package gov.nasa.jpl.aerielander.models.data;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

import java.util.List;

/** House keeping model to facilitate with instrument data modeling. */
public final class HKModel {

  // 52 bits/second; 0.1872 Mbits/hour
  public final InstrumentHKChannel APSS = new InstrumentHKChannel(DataConfig.APID.APID_CHAN_003, 0.1872, 0.1872);

  // 27 bits/second; 0.0972 Mbits/hour
  public final InstrumentHKChannel IDC = new InstrumentHKChannel(DataConfig.APID.APID_CHAN_005, 0.0972, 0.0972);

  // 31 bits/second; 0.1116 Mbits/hour
  public final InstrumentHKChannel IDA = new InstrumentHKChannel(DataConfig.APID.APID_CHAN_006, 0.1116, 0.1116);

  // 16 bits/second; 0.0576 Mbits/hour
  public final InstrumentHKChannel HeatProbe = new InstrumentHKChannel(DataConfig.APID.APID_CHAN_004, 0.0576, 0.0576);

  // 18 bits/second; 0.0648 Mbits/hour
  public final InstrumentHKChannel HeatProbe_NON_CHAN = new InstrumentHKChannel(DataConfig.APID.APID_HeatProbe_ENG, 0.0648, 0.0648);

  // 66 bits/second; 0.2376 Mbits/hour
  public final InstrumentHKChannel SEIS = new InstrumentHKChannel(DataConfig.APID.APID_CHAN_007, 0.2376, 0.2376);

  // 63 bits/second; 0.2268 Mbits/hour, no non-channelized data for diagnostic wakes
  public final InstrumentHKChannel SEIS_NON_CHAN = new InstrumentHKChannel(DataConfig.APID.APID_SEIS_ENG, 0.2268, 0.0);

  public final InstrumentHKChannel DUMP_CMD_HISTORY = new InstrumentHKChannel(DataConfig.APID.APID_DUMP_CMD_HISTORY, 0.3123, 0.3123);

  public final List<InstrumentHKChannel> allChannels = List.of(
      APSS,
      IDC,
      IDA,
      HeatProbe,
      HeatProbe_NON_CHAN,
      SEIS,
      SEIS_NON_CHAN,
      DUMP_CMD_HISTORY
  );

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete("%s/APSS/FullWakeRate".formatted(basePath), APSS.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/APSS/DiagnosticWakeRate".formatted(basePath), APSS.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/IDC/FullWakeRate".formatted(basePath), IDC.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/IDC/DiagnosticWakeRate".formatted(basePath), IDC.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/IDA/FullWakeRate".formatted(basePath), IDA.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/IDA/DiagnosticWakeRate".formatted(basePath), IDA.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/HeatProbe/FullWakeRate".formatted(basePath), HeatProbe.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/HeatProbe/DiagnosticWakeRate".formatted(basePath), HeatProbe.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/HeatProbe_NON_CHAN/FullWakeRate".formatted(basePath), HeatProbe_NON_CHAN.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/HeatProbe_NON_CHAN/DiagnosticWakeRate".formatted(basePath), HeatProbe_NON_CHAN.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/SEIS/FullWakeRate".formatted(basePath), SEIS.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/SEIS/DiagnosticWakeRate".formatted(basePath), SEIS.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/SEIS_NON_CHAN/FullWakeRate".formatted(basePath), SEIS_NON_CHAN.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/SEIS_NON_CHAN/DiagnosticWakeRate".formatted(basePath), SEIS_NON_CHAN.diagnosticWakeRate, new DoubleValueMapper());

    registrar.discrete("%s/DUMP_CMD_HISTORY/FullWakeRate".formatted(basePath), DUMP_CMD_HISTORY.fullWakeRate, new DoubleValueMapper());
    registrar.discrete("%s/DUMP_CMD_HISTORY/DiagnosticWakeRate".formatted(basePath), DUMP_CMD_HISTORY.diagnosticWakeRate, new DoubleValueMapper());
  }
}
