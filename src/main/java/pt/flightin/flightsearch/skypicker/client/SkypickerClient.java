package pt.flightin.flightsearch.skypicker.client;

import lombok.Builder;
import lombok.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.flightin.flightsearch.skypicker.client.model.Airline;
import pt.flightin.flightsearch.skypicker.client.model.FlightSearchResponse;
import pt.flightin.flightsearch.skypicker.client.model.LocationSearchResponse;

import java.util.List;

@FeignClient(name = "skypicker", url = "${client.skypicker.url}")
public interface SkypickerClient {

    @Cacheable("flights")
    @GetMapping(value = "/flights", produces = MediaType.APPLICATION_JSON_VALUE)
    FlightSearchResponse findFlights(@SpringQueryMap FlightParams flightParams);

    @Cacheable("airlines")
    @GetMapping(value = "/airlines", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Airline> findAirlines();

    @Cacheable("locations")
    @GetMapping(value = "/locations?location_types=airport&limit=1&active_only=true", produces = MediaType.APPLICATION_JSON_VALUE)
    LocationSearchResponse findLocation(@RequestParam("term") String term);

    @Value
    @Builder
    class FlightParams {

        String fly_from;
        String fly_to;
        String curr;
        String dateFrom;
        String dateTo;
        @Builder.Default
        String partner = "picky";
        String select_airlines;
        @Builder.Default
        Boolean select_airlines_exclude = Boolean.FALSE;
    }
}
