package client;

import client.cache.Cachable;
import com.weather.Day;
import com.weather.Region;
import service.WeatherForecasterService;

import java.util.Objects;

import static java.util.Objects.nonNull;

public class ForecasterClient {

    private final WeatherForecasterService weatherForecasterService;
    private final Cachable weatherCache;

    public ForecasterClient(WeatherForecasterService weatherForecasterService, Cachable weatherCache) {
        this.weatherForecasterService = weatherForecasterService;
        this.weatherCache = weatherCache;
    }

    public String getSummary(Region region, Day day) {

        String summary = weatherCache.getSummary(region, day);
        return nonNull(summary) ? summary : weatherForecasterService.getWeatherSummary(region, day);
    }

    public int getTemperature(Region region, Day day) {

        Integer temperature = weatherCache.getTemperature(region, day);
        return nonNull(temperature) ? temperature : weatherForecasterService.getTemperature(region, day);
    }
}
