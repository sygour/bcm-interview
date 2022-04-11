package infra.secondary;

import java.time.Instant;

public record HawesProduction(Instant start, Instant end, int power) {
}
