package domain;

import java.time.Instant;

public record PowerPlantProduction(Instant start, Instant end, int power) {
}
