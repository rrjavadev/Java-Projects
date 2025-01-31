package client.cache;

import java.time.Instant;

public record Temperature(int temperature, Instant createTimestamp) {
}
