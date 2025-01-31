package service;

import com.weather.Day;
import com.weather.Region;

public interface WeatherForecasterService {

    String getWeatherSummary(Region region, Day day);

    int getTemperature(Region region, Day day);
}
