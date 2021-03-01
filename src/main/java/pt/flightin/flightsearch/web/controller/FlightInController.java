package pt.flightin.flightsearch.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.port.in.FlightQuery;
import pt.flightin.flightsearch.web.api.FlightInApi;
import pt.flightin.flightsearch.web.mapper.FlightInMapper;

import java.util.Map;

@RestController
public class FlightInController implements FlightInApi {

    final FlightQuery query;
    final FlightInMapper mapper;

    @Autowired
    public FlightInController(FlightQuery query, FlightInMapper mapper) {
        this.query = query;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Map<String, FlightAverageData>> findDestinations(MultiValueMap<String, String> queryString) throws BaseException {
        FlightQuery.Params searchQueryParams = this.mapper.toFlightSearchQueryParams(queryString);
        Map<String, FlightAverageData> averageDataMap = this.query.findDestinations(searchQueryParams);
        return ResponseEntity.ok(averageDataMap);
    }
}
