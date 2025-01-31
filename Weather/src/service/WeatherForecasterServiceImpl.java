package service;

import com.weather.Day;
import com.weather.Forecaster;
import com.weather.Region;

public class WeatherForecasterServiceImpl implements WeatherForecasterService {

    private final Forecaster forecaster;

    public WeatherForecasterServiceImpl() {
        forecaster = new Forecaster();
    }

    @Override
    public String getWeatherSummary(Region region, Day day) {
        return forecaster.forecastFor(region, day)
                .summary();
    }

    @Override
    public int getTemperature(Region region, Day day) {
        return forecaster.forecastFor(region, day)
                .temperature();
    }
}
