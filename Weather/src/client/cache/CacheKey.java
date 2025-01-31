package client.cache;

import com.weather.Day;
import com.weather.Region;

public record CacheKey(Region region, Day day) {
}
