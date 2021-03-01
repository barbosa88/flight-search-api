package pt.flightin.flightsearch.skypicker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;
import pt.flightin.flightsearch.skypicker.client.SkypickerClient;
import pt.flightin.flightsearch.skypicker.client.model.FlightSearchResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(imports = {Stream.class, Collection.class})
public interface SkypickerMapper {

    @Mapping(target = "fly_from", source = "from")
    @Mapping(target = "fly_to", source = "to")
    @Mapping(target = "curr", source = "currency")
    @Mapping(target = "dateFrom", source = "dateFrom")
    @Mapping(target = "dateTo", source = "dateTo")
    @Mapping(target = "select_airlines", expression = "java(String.join(\",\", filter.getAirlineList()))")
    SkypickerClient.FlightParams toSkypickerClientParams(FlightRemotePort.Params filter);

    default List<FlightData> toFlightDataList(FlightSearchResponse response) {
        if (response == null) {
            return Collections.emptyList();
        }

        return Stream.ofNullable(response.getFlightDataList())
                     .flatMap(Collection::stream)
                     .map(data -> this.toFlightData(data, response.getCurrency()))
                     .collect(Collectors.toList());
    }

    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "destination", source = "flightData.destination")
    @Mapping(target = "price", source = "flightData.price")
    @Mapping(target = "bagPrice", source = "flightData.bagPrice")
    FlightData toFlightData(pt.flightin.flightsearch.skypicker.client.model.FlightData flightData, String currency);

    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", expression =
        "java(Stream.ofNullable(location.getNameList()).flatMap(Collection::stream).findFirst().orElse(null))")
    Location toLocation(pt.flightin.flightsearch.skypicker.client.model.Location location);

    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    Airline toAirline(pt.flightin.flightsearch.skypicker.client.model.Airline airline);
}
