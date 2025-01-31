package service;

import com.weather.Day;
import com.weather.Region;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class WeatherForecasterServiceImplIT {

    private final WeatherForecasterService weatherForecasterService = new WeatherForecasterServiceImpl();;

    @Test
    public void testGetWeatherSummary() {
        String weatherSummary = weatherForecasterService.getWeatherSummary(Region.LONDON, Day.TUESDAY);
        assertNotNull(weatherSummary);
    }

    @Test
    public void testGetTemperature() {
        int temperature = weatherForecasterService.getTemperature(Region.LONDON, Day.TUESDAY);
        assertNotEquals(-50, temperature);
    }
}