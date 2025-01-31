package client.cache;

import com.weather.Day;
import com.weather.Region;

public interface Cachable {

    String getSummary(Region region, Day day);

    void putSummary(Region region, Day day, String summary);

    Integer getTemperature(Region region, Day day);

    void putTemperature(Region region, Day day, Integer temperature);
}
