package infra.secondary;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class HawesPowerPlantTest {
  @Test
  void shouldGetData() {
    List<HawesProduction> data = new HawesPowerPlant().getData(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-05"));

    assertThat(data).hasSizeGreaterThan(0)
      .first()
      .extracting(HawesProduction::start, HawesProduction::end, HawesProduction::power)
      .doesNotContainNull();
  }
}
