package client.cache;

import java.time.Instant;

public record WeatherSummary(String summary, Instant createTimestamp) {
}
