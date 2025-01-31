package client.cache;

import com.weather.Day;
import com.weather.Region;

import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public class WeatherForecasterClientCache implements Cachable {

    private final int cacheLimit;
    private final Map<CacheKey, WeatherSummary> WEATHER_SUMMARY_CACHE;
    private final Map<CacheKey, Temperature> TEMPERATURE_CACHE;
    private Clock clock;

    public WeatherForecasterClientCache(int cacheLimit, Clock clock) {
        this.cacheLimit = cacheLimit;
        WEATHER_SUMMARY_CACHE = new LinkedHashMap<>(cacheLimit);
        TEMPERATURE_CACHE = new LinkedHashMap<>(cacheLimit);
        this.clock = clock;
    }

    public void alterClock(Clock clock){
        this.clock = clock;
    }

    @Override
    public String getSummary(Region region, Day day) {
        CacheKey key = new CacheKey(region, day);
        WeatherSummary weatherSummary = WEATHER_SUMMARY_CACHE.get(key);
        return isNull(weatherSummary) ? null : getSummary(weatherSummary, key);
    }

    @Override
    public void putSummary(Region region, Day day, String summary) {
        if (WEATHER_SUMMARY_CACHE.size() == cacheLimit) {
            removeOldestSummaryEntry();
        }
        CacheKey key = new CacheKey(region, day);
        WEATHER_SUMMARY_CACHE.put(key, new WeatherSummary(summary, clock.instant()));
    }

    @Override
    public Integer getTemperature(Region region, Day day) {
        CacheKey key = new CacheKey(region, day);
        Temperature temperature = TEMPERATURE_CACHE.get(key);

        return isNull(temperature) ? null : getTemperature(temperature, key);
    }

    private Integer getTemperature(Temperature temperature, CacheKey key) {
        if (clock.instant().compareTo(temperature.createTimestamp()) < 1) {
            return temperature.temperature();
        } else {
            TEMPERATURE_CACHE.remove(key);
            return null;
        }
    }

    private String getSummary(WeatherSummary weatherSummary, CacheKey key) {
        if (clock.instant().compareTo(weatherSummary.createTimestamp()) < 1) {
            return weatherSummary.summary();
        } else {
            WEATHER_SUMMARY_CACHE.remove(key);
            return null;
        }
    }

    @Override
    public void putTemperature(Region region, Day day, Integer temperature) {
        if (TEMPERATURE_CACHE.size() == cacheLimit) {
            removeOldestTemperatureCacheEntry();
        }
        TEMPERATURE_CACHE.put(new CacheKey(region, day), new Temperature(temperature, clock.instant()));
    }

    private void removeOldestSummaryEntry() {
        Map.Entry<CacheKey, WeatherSummary> firstEntry = WEATHER_SUMMARY_CACHE.entrySet().iterator().next();
        WEATHER_SUMMARY_CACHE.remove(firstEntry.getKey());
    }

    private void removeOldestTemperatureCacheEntry() {
        Map.Entry<CacheKey, Temperature> firstEntry = TEMPERATURE_CACHE.entrySet().iterator().next();
        TEMPERATURE_CACHE.remove(firstEntry.getKey());
    }
}
