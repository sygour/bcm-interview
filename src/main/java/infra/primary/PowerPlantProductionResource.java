package infra.primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.PowerPlanException;
import domain.PowerPlantProductionList;
import domain.PowerPlantProductionUseCase;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class PowerPlantProductionResource {
  private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  // TODO enable new format: csv
  private static final Set<String> AVAILABLE_FORMATS = Set.of("json");

  private final PowerPlantProductionUseCase useCase;
  private final ObjectMapper jsonMapper;

  PowerPlantProductionResource(PowerPlantProductionUseCase useCase, ObjectMapper objectMapper) {
    this.useCase = useCase;
    this.jsonMapper = objectMapper;
  }

  public PowerPlantProductionResource() {
    this(
      new PowerPlantProductionUseCase(),
      new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    );
  }

  public String aggregateProductions(String from, String to, String format) {
    if (!AVAILABLE_FORMATS.contains(format)) {
      throw new IllegalArgumentException("expecting (json|csv), received: " + format);
    }
    checkDateFormat(from);
    checkDateFormat(to);

    LocalDate fromDate = INPUT_DATE_FORMATTER.parse(from, LocalDate::from);
    LocalDate toDate = INPUT_DATE_FORMATTER.parse(to, LocalDate::from);
    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("invalid range: from date should be before to date");
    }

    PowerPlantProductionList productionList = useCase.aggregateProductions(fromDate, toDate);

    try {
      return jsonMapper.writeValueAsString(productionList.productions());
    } catch (JsonProcessingException e) {
      throw new PowerPlanException(e);
    }
  }

  private void checkDateFormat(String date) {
    if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
      throw new IllegalArgumentException("expecting (DD-MM-YYYY) date, received: " + date);
    }
  }
}
