package infra.primary;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.PowerPlantProduction;
import domain.PowerPlantProductionList;
import domain.PowerPlantProductionUseCase;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class PowerPlantProductionResourceTest {
  private final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
  private final PowerPlantProductionUseCase useCase = Mockito.mock(PowerPlantProductionUseCase.class);
  private final PowerPlantProductionResource resource = new PowerPlantProductionResource(useCase, objectMapper);

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"txt", "html"})
  void shouldAllowOnlyJsonAndCsv(String format) {
    assertThatThrownBy(() -> resource.aggregateProductions("", "", format)).isNotNull();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"0", "00-00-0000", "45-01-2000", "01-45-2000"})
  void shouldNotAcceptInvalidDate(String date) {
    assertThatThrownBy(() -> resource.aggregateProductions(date, "05-01-2020", "json")).isNotNull();
  }

  @Test
  void shouldNotAcceptInvalidRange() {
    assertThatThrownBy(() -> resource.aggregateProductions("15-11-2020", "05-01-2020", "json")).isNotNull();
  }

  @Test
  void shouldDisplayProductionAsString() throws JsonProcessingException {
    Mockito.when(useCase.aggregateProductions(Mockito.any(), Mockito.any()))
      .thenReturn(new PowerPlantProductionList(List.of(new PowerPlantProduction(Instant.EPOCH, Instant.EPOCH.plusSeconds(15 * 60), 200))));
    Mockito.when(objectMapper.writeValueAsString(Mockito.any()))
      .thenReturn("[{\"start\":\"1970-01-01T00:00:00Z\",\"end\":\"1970-01-01T00:15:00Z\",\"power\":200}]");

    String json = resource.aggregateProductions("01-01-2020", "05-01-2020", "json");

    assertThat(json).isEqualTo("[{\"start\":\"1970-01-01T00:00:00Z\",\"end\":\"1970-01-01T00:15:00Z\",\"power\":200}]");
  }
}
