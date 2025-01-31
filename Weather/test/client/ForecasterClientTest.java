package client;

import client.cache.Cachable;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Test;
import service.WeatherForecasterService;

import static com.weather.Day.*;
import static com.weather.Region.LONDON;
import static org.junit.Assert.assertEquals;

public class ForecasterClientTest {

    private ForecasterClient forecasterClient;
    private final JUnitRuleMockery context = new JUnitRuleMockery();
    WeatherForecasterService forecasterServiceMck = context.mock(WeatherForecasterService.class);
    Cachable clientCacheMock = context.mock(Cachable.class);

    @Before
    public void setUp() {
        forecasterClient = new ForecasterClient(forecasterServiceMck, clientCacheMock);
    }

    @Test
    public void shouldGetWeatherForecastSummaryWhenCacheDoesNotHaveData(){

        //Given
        context.checking(new Expectations() {{
            oneOf(forecasterServiceMck).getWeatherSummary(with(LONDON), with(MONDAY));
            will(returnValue("Nice weather"));
            oneOf(clientCacheMock).getSummary(LONDON, MONDAY);
            will(returnValue(null));
        }});

        //When
        String summary = forecasterClient.getSummary(LONDON, MONDAY);

        //Then
        assertEquals("Nice weather", summary);
    }

    @Test
    public void shouldGetWeatherForecastSummaryFromCache(){

        //Given
        context.checking(new Expectations() {{
            oneOf(clientCacheMock).getSummary(with(LONDON), with(MONDAY));
            will(returnValue("Nice weather"));
        }});

        //When
        String summary = forecasterClient.getSummary(LONDON, MONDAY);

        //Then
        assertEquals("Nice weather", summary);
    }

    @Test
    public  void shouldGetTemperatureWhenCacheDoesNotHaveData(){

        //Given
        context.checking(new Expectations(){{
            oneOf(forecasterServiceMck).getTemperature(LONDON, MONDAY);
            will(returnValue(12));
            oneOf(clientCacheMock).getTemperature(LONDON, MONDAY);
            will(returnValue(null));
        }});

        //When
        int temperature = forecasterClient.getTemperature(LONDON, MONDAY);

        //Then
        assertEquals(12, temperature);
    }

    @Test
    public  void shouldGetTemperatureFromCache(){

        //Given
        context.checking(new Expectations(){{
            oneOf(clientCacheMock).getTemperature(LONDON, MONDAY);
            will(returnValue(12));
        }});

        //When
        int temperature = forecasterClient.getTemperature(LONDON, MONDAY);

        //Then
        assertEquals(12, temperature);
    }
}