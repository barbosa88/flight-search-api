package pt.flightin.flightsearch.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.lang.Nullable;
import pt.flightin.flightsearch.core.domain.BagPrice;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.port.in.FlightQuery;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Mapper
public interface FlightMapper {

    @Mapping(target = "from", source = "from")
    @Mapping(target = "to", source = "to")
    @Mapping(target = "currency", source = "params.currency")
    @Mapping(target = "dateFrom", source = "params.dateFrom")
    @Mapping(target = "dateTo", source = "params.dateTo")
    @Mapping(target = "airlineList", source = "params.airlineList")
    FlightRemotePort.Params toFlightSearchRemotePortParams(String from, String to, FlightQuery.Params params);

    @Mapping(target = "airport", source = "airport")
    @Mapping(target = "currency", source = "filter.currency")
    @Mapping(target = "dateFrom", source = "filter.dateFrom")
    @Mapping(target = "dateTo", source = "filter.dateTo")
    @Mapping(target = "priceAverage", source = "flightDataList", qualifiedByName = "toPriceAverage")
    @Mapping(target = "bagAverage.bag1Average", source = "flightDataList", qualifiedByName = "toBag1Average")
    @Mapping(target = "bagAverage.bag2Average", source = "flightDataList", qualifiedByName = "toBag2Average")
    FlightAverageData toFlightAverageData(FlightQuery.Params filter, String airport, List<FlightData> flightDataList);

    @Named("toPriceAverage")
    @Nullable
    default BigDecimal toPriceAverage(List<FlightData> flightDataList) {
        OptionalDouble average = Stream.of(flightDataList).flatMap(Collection::stream)
                                       .map(FlightData::getPrice)
                                       .mapToDouble(BigDecimal::doubleValue).average();

        return average.isPresent()
            ? BigDecimal.valueOf(average.getAsDouble()).setScale(2, RoundingMode.CEILING)
            : null;
    }

    @Named("toBag1Average")
    @Nullable
    default BigDecimal toBag1Average(List<FlightData> flightDataList) {
        OptionalDouble average = Stream.of(flightDataList).flatMap(Collection::stream)
                                       .map(FlightData::getBagPrice).map(BagPrice::getBag1Price).filter(Objects::nonNull)
                                       .mapToDouble(BigDecimal::doubleValue).average();

        return average.isPresent()
            ? BigDecimal.valueOf(average.getAsDouble()).setScale(2, RoundingMode.CEILING)
            : null;
    }

    @Named("toBag2Average")
    @Nullable
    default BigDecimal toBag2Average(List<FlightData> flightDataList) {
        OptionalDouble average = Stream.of(flightDataList).flatMap(Collection::stream)
                                       .map(FlightData::getBagPrice).map(BagPrice::getBag2Price).filter(Objects::nonNull)
                                       .mapToDouble(BigDecimal::doubleValue).average();

        return average.isPresent()
            ? BigDecimal.valueOf(average.getAsDouble()).setScale(2, RoundingMode.CEILING)
            : null;
    }
}
