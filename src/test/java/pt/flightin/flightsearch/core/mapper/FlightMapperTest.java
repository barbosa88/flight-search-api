package pt.flightin.flightsearch.core.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pt.flightin.flightsearch.core.domain.BagAveragePrice;
import pt.flightin.flightsearch.core.domain.BagPrice;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.port.in.FlightQuery;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

class FlightMapperTest {

    static FlightMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(FlightMapper.class);
    }

    @Test
    void toFlightSearchRemotePortParams_ValidInput_IsProperlyMapped() {
        FlightQuery.Params filter = this.buildFlightQueryParams();
        FlightRemotePort.Params expected =
            FlightRemotePort.Params.builder()
                                   .currency("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                                   .airlineList(List.of("TP", "FR")).from("OPO").to("LIS")
                                   .build();

        FlightRemotePort.Params result = mapper.toFlightSearchRemotePortParams("OPO", "LIS", filter);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
    }

    private FlightQuery.Params buildFlightQueryParams() {
        return FlightQuery.Params.builder()
                                 .currency("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                                 .airlineList(List.of("TP", "FR")).destinationList(List.of("OPO", "LIS"))
                                 .build();
    }

    @Test
    void toFlightAverageData_ValidInput_IsProperlyMapped() {
        FlightQuery.Params filter = this.buildFlightQueryParams();
        List<FlightData> flightDataList = List.of(
            this.buildFlightData("EUR", "OPO", BigDecimal.valueOf(10), BigDecimal.valueOf(5), BigDecimal.valueOf(10)),
            this.buildFlightData("EUR", "OPO", BigDecimal.valueOf(15), BigDecimal.valueOf(10), null)
        );

        FlightAverageData expected =
            FlightAverageData.builder().currency("EUR").dateFrom("01/01/2021").dateTo("02/01/2021")
                             .priceAverage(BigDecimal.valueOf(12.5).setScale(2, RoundingMode.CEILING)).airport("Sa Carneiro")
                             .bagAverage(BagAveragePrice.builder()
                                                        .bag1Average(BigDecimal.valueOf(7.5).setScale(2, RoundingMode.CEILING))
                                                        .bag2Average(BigDecimal.valueOf(10).setScale(2, RoundingMode.CEILING))
                                                        .build())
                             .build();
        FlightAverageData result = mapper.toFlightAverageData(filter, "Sa Carneiro", flightDataList);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected, result);
    }

    private FlightData buildFlightData(String curr, String dest, BigDecimal price, BigDecimal bag1, BigDecimal bag2) {
        return FlightData.builder()
                         .currency(curr).destination(dest).price(price)
                         .bagPrice(BagPrice.builder().bag1Price(bag1).bag2Price(bag2).build())
                         .build();
    }
}