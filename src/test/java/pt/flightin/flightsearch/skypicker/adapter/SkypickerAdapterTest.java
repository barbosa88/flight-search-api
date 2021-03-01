package pt.flightin.flightsearch.skypicker.adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;
import pt.flightin.flightsearch.skypicker.client.SkypickerClient;
import pt.flightin.flightsearch.skypicker.client.model.Airline;
import pt.flightin.flightsearch.skypicker.client.model.FlightSearchResponse;
import pt.flightin.flightsearch.skypicker.client.model.Location;
import pt.flightin.flightsearch.skypicker.client.model.LocationSearchResponse;
import pt.flightin.flightsearch.skypicker.mapper.SkypickerMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class SkypickerAdapterTest {

    @Mock
    SkypickerClient client;
    @Mock
    SkypickerMapper mapper;
    @InjectMocks
    SkypickerAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAirline_ValidInput_CallDependencies() {
        Airline airline = new Airline();
        airline.setCode("TP");
        airline.setName("TAP");

        Mockito.when(this.client.findAirlines())
               .thenReturn(List.of(airline));

        Mockito.when(this.mapper.toAirline(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(pt.flightin.flightsearch.core.domain.Airline.class));

        Optional<pt.flightin.flightsearch.core.domain.Airline> result = this.adapter.findAirline("TP");
        Assertions.assertTrue(result.isPresent());

        Mockito.verify(this.client, Mockito.times(1))
               .findAirlines();
        Mockito.verify(this.mapper, Mockito.times(1))
               .toAirline(ArgumentMatchers.any());
    }

    @Test
    void findAirline_AirlineNotFound_ReturnsEmptyOptional() {
        Mockito.when(this.client.findAirlines())
               .thenReturn(Collections.emptyList());

        Optional<pt.flightin.flightsearch.core.domain.Airline> result = this.adapter.findAirline("TP");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void findLocation_ValidInput_CallDependencies() {
        Location location = new Location();
        location.setCode("OPO");
        location.setNameList(List.of("Porto"));
        LocationSearchResponse locationSearchResponse = new LocationSearchResponse();
        locationSearchResponse.setLocationList(List.of(location));

        Mockito.when(this.client.findLocation("OPO"))
               .thenReturn(locationSearchResponse);

        Mockito.when(this.mapper.toLocation(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(pt.flightin.flightsearch.core.domain.Location.class));

        Optional<pt.flightin.flightsearch.core.domain.Location> result = this.adapter.findLocation("OPO");
        Assertions.assertTrue(result.isPresent());

        Mockito.verify(this.client, Mockito.times(1))
               .findLocation("OPO");
        Mockito.verify(this.mapper, Mockito.times(1))
               .toLocation(ArgumentMatchers.any());
    }

    @Test
    void findLocation_LocationNotFound_ReturnsEmptyOptional() {
        Mockito.when(this.client.findLocation("OPO"))
               .thenReturn(null);

        Optional<pt.flightin.flightsearch.core.domain.Airline> result = this.adapter.findAirline("TP");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void findFlights_NoFlightsFound_ReturnsEmptyList() {
        Mockito.when(this.mapper.toSkypickerClientParams(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(SkypickerClient.FlightParams.class));

        FlightRemotePort.Params params = this.buildFlightRemotePortParams();
        List<FlightData> result = this.adapter.findFlights(params);

        Assertions.assertTrue(result.isEmpty());
    }

    private FlightRemotePort.Params buildFlightRemotePortParams() {
        return FlightRemotePort.Params.builder()
                                      .currency("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                                      .airlineList(List.of("TP", "FR")).from("OPO").to("LIS")
                                      .build();
    }

    @Test
    void findFlights_FlightsFound_ReturnsFlightDataList() {
        Mockito.when(this.mapper.toSkypickerClientParams(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(SkypickerClient.FlightParams.class));

        Mockito.when(this.client.findFlights(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(FlightSearchResponse.class));

        Mockito.when(this.mapper.toFlightDataList(ArgumentMatchers.any()))
               .thenReturn(List.of(Mockito.mock(FlightData.class)));

        FlightRemotePort.Params params = this.buildFlightRemotePortParams();
        List<FlightData> result = this.adapter.findFlights(params);

        Assertions.assertFalse(result.isEmpty());
    }
}