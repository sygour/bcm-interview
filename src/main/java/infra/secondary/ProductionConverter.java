package infra.secondary;

import domain.PowerPlantProduction;

public class ProductionConverter {
  public PowerPlantProduction from(HawesProduction hawesProduction) {
    return new PowerPlantProduction(hawesProduction.start(), hawesProduction.end(), hawesProduction.power());
  }
}
