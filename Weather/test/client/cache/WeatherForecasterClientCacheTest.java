package client.cache;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.weather.Day.MONDAY;
import static com.weather.Day.TUESDAY;
import static com.weather.Day.WEDNESDAY;
import static com.weather.Region.LONDON;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.Assert.assertEquals;

public class WeatherForecasterClientCacheTest {

    private static final String CURRENT_TIMESTAMP = "2023-12-01T10:15:30.00Z";
    private static final String SUNNY = "Sunny";
    private final Clock clock = Clock.fixed(Instant.parse(CURRENT_TIMESTAMP), UTC);
    private final WeatherForecasterClientCache cache = new WeatherForecasterClientCache(1000, clock);

    @Test
    public void shouldAddSummaryRequestsToCache() {

        //Given
        cache.putSummary(LONDON, MONDAY, SUNNY);

        //When
        String summary = cache.getSummary(LONDON, MONDAY);

        //Then
        assertEquals(SUNNY, summary);
    }

    @Test
    public void shouldAddTemperatureRequestsToCache() {

        //Given
        cache.putTemperature(LONDON, MONDAY, 30);

        //When
        Integer temperature = cache.getTemperature(LONDON, MONDAY);

        //Then
        assertEquals((Integer) 30, temperature);
    }

    @Test
    public void shouldDeleteOldEntryWhenSummaryCacheLimitIsExceeded() {

        //Given
        WeatherForecasterClientCache cache = new WeatherForecasterClientCache(1, clock);

        cache.putSummary(LONDON, MONDAY, SUNNY);
        cache.putSummary(LONDON, WEDNESDAY, SUNNY);
        cache.putSummary(LONDON, TUESDAY, "Rainy");

        //When
        String summary = cache.getSummary(LONDON, MONDAY);

        //Then
        assertEquals(null, summary);
    }

    @Test
    public void shouldDeleteOldEntryWhenTemperatureCacheLimitIsExceeded() {

        //Given
        WeatherForecasterClientCache cache = new WeatherForecasterClientCache(1, clock);

        cache.putTemperature(LONDON, MONDAY, 10);
        cache.putTemperature(LONDON, WEDNESDAY, 11);
        cache.putTemperature(LONDON, TUESDAY, 12);

        //When
        Integer temperature = cache.getTemperature(LONDON, MONDAY);

        //Then
        assertEquals(null, temperature);
    }

    @Test
    public void shouldRefreshSummaryCacheWhenTimeToLiveExceedsOneHour() {

        //Given
        Instant currentTimestamp = Instant.parse(CURRENT_TIMESTAMP);
        Clock clock = Clock.fixed(currentTimestamp, UTC);

        WeatherForecasterClientCache cache = new WeatherForecasterClientCache(100, clock);
        cache.putSummary(LONDON, MONDAY, SUNNY);

        assertEquals(cache.getSummary(LONDON, MONDAY), SUNNY);

        //When
        cache.alterClock(Clock.fixed(currentTimestamp.plus(1L, HOURS), UTC));

        //Then
        assertEquals(cache.getSummary(LONDON, MONDAY), null);
    }

    @Test
    public void shouldRefreshTemperatureCacheWhenTimeToLiveExceedsOneHour() {

        //Given
        Instant currentTimestamp = Instant.parse(CURRENT_TIMESTAMP);
        Clock clock = Clock.fixed(currentTimestamp, UTC);

        WeatherForecasterClientCache cache = new WeatherForecasterClientCache(100, clock);
        cache.putTemperature(LONDON, MONDAY, 10);

        assertEquals(cache.getTemperature(LONDON, MONDAY), Integer.valueOf(10));

        //After One hour
        cache.alterClock(Clock.fixed(currentTimestamp.plus(1L, HOURS), UTC));

        //Then
        assertEquals(cache.getTemperature(LONDON, MONDAY), null);
    }
}