package pt.flightin.flightsearch.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.MultiValueMap;
import pt.flightin.flightsearch.core.port.in.FlightQuery;

@Mapper
public interface FlightInMapper {

    @Mapping(target = "destinationList", expression = "java(queryString.get(\"dest\"))")
    @Mapping(target = "airlineList", expression = "java(queryString.get(\"airline\"))")
    @Mapping(target = "currency", expression = "java(queryString.getFirst(\"curr\"))")
    @Mapping(target = "dateFrom", expression = "java(queryString.getFirst(\"dateFrom\"))")
    @Mapping(target = "dateTo", expression = "java(queryString.getFirst(\"dateTo\"))")
    FlightQuery.Params toFlightSearchQueryParams(MultiValueMap<String, String> queryString);
}
