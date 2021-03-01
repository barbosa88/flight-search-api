package pt.flightin.flightsearch.skypicker.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;
import pt.flightin.flightsearch.skypicker.client.SkypickerClient;
import pt.flightin.flightsearch.skypicker.client.model.FlightSearchResponse;
import pt.flightin.flightsearch.skypicker.client.model.LocationSearchResponse;
import pt.flightin.flightsearch.skypicker.mapper.SkypickerMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SkypickerAdapter implements FlightRemotePort {

    final SkypickerClient client;
    final SkypickerMapper mapper;

    @Autowired
    public SkypickerAdapter(SkypickerClient client, SkypickerMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Optional<Airline> findAirline(String airline) {
        return Stream.ofNullable(this.client.findAirlines())
                     .flatMap(Collection::stream)
                     .filter(arl -> Objects.equals(airline, arl.getCode())).findFirst()
                     .map(this.mapper::toAirline);
    }

    @Override
    public List<FlightData> findFlights(FlightRemotePort.Params filter) {
        SkypickerClient.FlightParams flightParams = this.mapper.toSkypickerClientParams(filter);
        FlightSearchResponse response = this.client.findFlights(flightParams);
        return this.mapper.toFlightDataList(response);
    }

    @Override
    public Optional<Location> findLocation(String location) {
        return Optional.ofNullable(this.client.findLocation(location))
                       .map(LocationSearchResponse::getLocationList)
                       .orElseGet(Collections::emptyList)
                       .stream().filter(loc -> Objects.equals(location, loc.getCode())).findFirst()
                       .map(this.mapper::toLocation);
    }
}
