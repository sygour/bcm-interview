package domain;

import infra.secondary.HawesPowerPlant;
import infra.secondary.ProductionConverter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PowerPlantProductionUseCase {
  private final ProductionConverter productionConverter;
  private final HawesPowerPlant hawesPowerPlant;

  public PowerPlantProductionUseCase() {
    this(
      new ProductionConverter(),
      new HawesPowerPlant()
    );
  }

  PowerPlantProductionUseCase(ProductionConverter productionConverter, HawesPowerPlant hawesPowerPlant) {
    this.productionConverter = productionConverter;
    this.hawesPowerPlant = hawesPowerPlant;
  }

  public PowerPlantProductionList aggregateProductions(LocalDate from, LocalDate to) {
    List<PowerPlantProduction> hawesProductionList = hawesPowerPlant.getData(from, to)
      .stream()
      .map(productionConverter::from)
      .collect(Collectors.toList());

    return new PowerPlantProductionList(hawesProductionList);
  }
}
