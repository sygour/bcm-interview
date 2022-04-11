package domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class PowerPlantProductionListTest {
  @Test
  void shouldNotModifyEmptyList() {
    PowerPlantProductionList productionList = new PowerPlantProductionList(List.of());

    assertThat(productionList.productions()).isEmpty();
  }

  @Test
  void shouldNotModifyCompleteList() {
    PowerPlantProductionList productionList = new PowerPlantProductionList(List.of(
      new PowerPlantProduction(Instant.EPOCH, Instant.EPOCH.plusSeconds(15*60), 100),
      new PowerPlantProduction(Instant.EPOCH.plusSeconds(15*60), Instant.EPOCH.plusSeconds(30*60), 200)
    ));

    assertThat(productionList.productions()).hasSize(2)
      .extracting(PowerPlantProduction::power)
      .containsExactly(100, 200);
  }

  @Test
  void shouldCompleteListWithHoles() {
    PowerPlantProductionList productionList = new PowerPlantProductionList(List.of(
      new PowerPlantProduction(Instant.EPOCH, Instant.EPOCH.plusSeconds(15*60), 100),
      new PowerPlantProduction(Instant.EPOCH.plusSeconds(15*60), Instant.EPOCH.plusSeconds(30*60), 200),
      // missing 30-45
      new PowerPlantProduction(Instant.EPOCH.plusSeconds(45*60), Instant.EPOCH.plusSeconds(60*60), 300),
      new PowerPlantProduction(Instant.EPOCH.plusSeconds(60*60), Instant.EPOCH.plusSeconds(75*60), 400)
    ));

    assertThat(productionList.productions()).hasSize(5)
      .extracting(PowerPlantProduction::power)
      .containsExactly(100, 200, 250, 300, 400);
  }
}
