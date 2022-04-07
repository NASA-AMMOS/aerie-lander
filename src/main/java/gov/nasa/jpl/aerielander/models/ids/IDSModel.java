package gov.nasa.jpl.aerielander.models.ids;

import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.BooleanValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;

public final class IDSModel {

  public enum IDAMode {
    Idle,
    Moving,
    Grappling
  }

  private final Register<IDAMode> idaMode;
  private final Register<Boolean> idaSurvivalHeatersNominal;

  public IDSModel() {
    idaMode = Register.forImmutable(IDAMode.Idle);
    idaSurvivalHeatersNominal = Register.forImmutable(true);
  }

  public void registerResources(final Registrar registrar, final String basePath) {
    registrar.discrete("%s/IDAMode".formatted(basePath), idaMode, new EnumValueMapper<>(IDAMode.class));
    registrar.discrete("%s/IDAMode".formatted(basePath), idaSurvivalHeatersNominal, new BooleanValueMapper());
  }

  public void setIDAMode(final IDAMode mode) {
    idaMode.set(mode);
  }

  public IDAMode getIDAMode() {
    return idaMode.get();
  }

  public void setIdaSurvivalHeatersNominal(final boolean value) {
    idaSurvivalHeatersNominal.set(value);
  }

  public boolean areIDASurvivalHeatersNominal() {
    return idaSurvivalHeatersNominal.get();
  }

  public static double computeSize(final int compQuality) {
    final double a =  30226.95970160277;
    final double b = -0.06658743610336151;
    final double c = -1459.444507542347;
    final double d =  0.002534592140626373;
    final double e =  80.74971322012104;
    final double f = -4.635963037609322E-05;
    final double g = -1.35191059095006;
    final double h =  3.855556487825687E-07;
    final double i =  0.006743543630872695;
    final double j = -1.187301787888751E-09;
    final int k = 0;
    final int l = 1;
    final double m = 1200.;
    final double n = 1648.;
    final int o = 0;
    final int x = compQuality;
    final int x2 = (int)Math.pow(compQuality, 2);
    final int x3 = (int)Math.pow(compQuality, 3);
    final int x4 = (int)Math.pow(compQuality, 4);
    final int x5 = (int)Math.pow(compQuality, 5);

    final double image_size;
    if ( compQuality == 0) {
      image_size = 1024*1024*16;
    }
    else{
      image_size = (((a+c*x+e*x2+g*x3+i*x4)/(1+b*x+d*x2+f*x3+h*x4+j*x5))*(k + (l*1024*1024)/(m*n)) + o)*8.;
    }
    return image_size/1.0e6;
  }
}
