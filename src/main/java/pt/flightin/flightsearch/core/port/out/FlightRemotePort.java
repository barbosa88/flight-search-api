package pt.flightin.flightsearch.core.port.out;

import lombok.Builder;
import lombok.Value;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.domain.Location;

import java.util.List;
import java.util.Optional;

public interface FlightRemotePort {

    Optional<Airline> findAirline(String airline);

    List<FlightData> findFlights(Params filter);

    Optional<Location> findLocation(String location);

    @Value
    @Builder
    class Params {

        String from;
        String to;
        String currency;
        String dateFrom;
        String dateTo;
        List<String> airlineList;
    }
}
