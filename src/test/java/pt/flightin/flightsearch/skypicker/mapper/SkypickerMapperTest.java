package pt.flightin.flightsearch.skypicker.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.domain.BagPrice;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;
import pt.flightin.flightsearch.skypicker.client.SkypickerClient;
import pt.flightin.flightsearch.skypicker.client.model.FlightSearchResponse;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

class SkypickerMapperTest {

    static SkypickerMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(SkypickerMapper.class);
    }

    @Test
    void toAirline_ValidInput_IsProperlyMapped() {
        pt.flightin.flightsearch.skypicker.client.model.Airline airline = new pt.flightin.flightsearch.skypicker.client.model.Airline();
        airline.setCode("FR");
        airline.setName("Ryanair");

        Airline expected = Airline.builder().code("FR").name("Ryanair").build();
        Airline result = mapper.toAirline(airline);

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toAirline(null));
    }

    @Test
    void toLocation_ValidInput_IsProperlyMapped() {
        pt.flightin.flightsearch.skypicker.client.model.Location location = new pt.flightin.flightsearch.skypicker.client.model.Location();
        location.setCode("OPO");
        location.setNameList(List.of("Sa Carneiro"));

        Location expected = Location.builder().code("OPO").name("Sa Carneiro").build();
        Location result = mapper.toLocation(location);

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toLocation(null));
    }

    @Test
    void toSkypickerClientParams_ValidInput_IsProperlyMapped() {
        FlightRemotePort.Params params =
            FlightRemotePort.Params.builder()
                                   .currency("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                                   .airlineList(List.of("TP", "FR")).from("OPO").to("LIS")
                                   .build();

        SkypickerClient.FlightParams expected =
            SkypickerClient.FlightParams.builder()
                                        .curr("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                                        .select_airlines("TP,FR").fly_from("OPO").fly_to("LIS")
                                        .build();
        SkypickerClient.FlightParams result = mapper.toSkypickerClientParams(params);

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toSkypickerClientParams(null));
    }

    @Test
    void toFlightDataList_ValidInput_IsProperlyMapped() {
        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        flightSearchResponse.setCurrency("EUR");
        flightSearchResponse.setFlightDataList(List.of(
            this.buildRemoteFlightData("EUR", "OPO", BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(10)),
            this.buildRemoteFlightData("EUR", "OPO", BigDecimal.valueOf(15), BigDecimal.valueOf(10), null)
        ));

        List<FlightData> expected = List.of(
            this.buildFlightData("EUR", "OPO", BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(10)),
            this.buildFlightData("EUR", "OPO", BigDecimal.valueOf(15), BigDecimal.valueOf(10), null)
        );
        List<FlightData> result = mapper.toFlightDataList(flightSearchResponse);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(Collections.emptyList(), mapper.toFlightDataList(null));
    }

    private pt.flightin.flightsearch.skypicker.client.model.FlightData buildRemoteFlightData(String curr, String dest,
        BigDecimal price, BigDecimal bag1, BigDecimal bag2) {
        pt.flightin.flightsearch.skypicker.client.model.BagPrice bagPrice = new pt.flightin.flightsearch.skypicker.client.model.BagPrice();
        bagPrice.setBag1Price(bag1);
        bagPrice.setBag2Price(bag2);

        pt.flightin.flightsearch.skypicker.client.model.FlightData flightData = new pt.flightin.flightsearch.skypicker.client.model.FlightData();
        flightData.setDestination(dest);
        flightData.setPrice(price);
        flightData.setBagPrice(bagPrice);

        return flightData;
    }

    private FlightData buildFlightData(String curr, String dest, BigDecimal price, BigDecimal bag1, BigDecimal bag2) {
        return FlightData.builder()
                         .currency(curr).destination(dest).price(price)
                         .bagPrice(BagPrice.builder().bag1Price(bag1).bag2Price(bag2).build())
                         .build();
    }
}