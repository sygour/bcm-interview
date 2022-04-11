package infra.secondary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.PowerPlanException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class HawesPowerPlant {
  private final ObjectMapper jsonMapper;
  private final DateTimeFormatter formatter;

  public HawesPowerPlant() {
    jsonMapper = new ObjectMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  }

  public List<HawesProduction> getData(LocalDate from, LocalDate to) {
    String fromParam = formatter.format(from);
    String toParam = formatter.format(to);

    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create("https://interview.beta.bcmenergy.fr/hawes?from=" + fromParam + "&to=" + toParam))
      .GET()
      .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      String json = response.body();
      return jsonMapper.readValue(json, new TypeReference<>() {});
    } catch (IOException | InterruptedException e) {
      // TODO manage exception and retry
      throw new PowerPlanException(e);
    }
  }
}
