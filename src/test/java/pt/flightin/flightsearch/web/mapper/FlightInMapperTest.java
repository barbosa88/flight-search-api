package pt.flightin.flightsearch.web.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pt.flightin.flightsearch.core.port.in.FlightQuery;

import java.util.List;

class FlightInMapperTest {

    static FlightInMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(FlightInMapper.class);
    }

    @Test
    void toFlightSearchQueryParams_ValidInput_IsProperlyMapped() {
        MultiValueMap<String, String> queryString = new LinkedMultiValueMap<>();
        queryString.put("dest", List.of("OPO", "LX"));
        queryString.put("airline", List.of("FR", "TP"));
        queryString.put("curr", List.of("EUR"));
        queryString.put("dateFrom", List.of("01/01/2021"));
        queryString.put("dateTo", List.of("02/01/2021"));

        FlightQuery.Params expected =
            FlightQuery.Params.builder()
                              .destinationList(List.of("OPO", "LX")).airlineList(List.of("FR", "TP"))
                              .dateFrom("01/01/2021").dateTo("02/01/2021").currency("EUR")
                              .build();
        FlightQuery.Params result = mapper.toFlightSearchQueryParams(queryString);

        Assertions.assertEquals(expected, result);
        Assertions.assertNotNull(mapper.toFlightSearchQueryParams(queryString));
    }
}