package domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

record PowerPlantProductionList(List<PowerPlantProduction> productions) {
  PowerPlantProductionList(List<PowerPlantProduction> productions) {
    this.productions = new ArrayList<>();
    if (productions != null && !productions.isEmpty()) {
      Iterator<PowerPlantProduction> iterator = productions.iterator();
      PowerPlantProduction first = iterator.next();
      PowerPlantProduction second;
      this.productions.add(first);
      while (iterator.hasNext()) {
        second = iterator.next();
        if (first.end().compareTo(second.start()) != 0) {
          this.productions.add(new PowerPlantProduction(first.end(), second.start(), (first.power() + second.power()) / 2));
        }
        this.productions.add(second);
        first = second;
      }
    }
  }
}
