package pt.flightin.flightsearch.core.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.flightin.flightsearch.core.domain.CodeType;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.domain.FlightData;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.mapper.FlightMapper;
import pt.flightin.flightsearch.core.port.in.FlightQuery;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;
import pt.flightin.flightsearch.core.validation.ApiCodeValidator;
import pt.flightin.flightsearch.core.validation.ApiCodeValidatorFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightQueryAdapter implements FlightQuery {

    final FlightRemotePort remotePort;
    final FlightMapper mapper;
    final ApiCodeValidatorFactory validatorFactory;

    @Autowired
    public FlightQueryAdapter(FlightRemotePort remotePort, FlightMapper mapper,
        ApiCodeValidatorFactory validatorFactory) {
        this.remotePort = remotePort;
        this.mapper = mapper;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Map<String, FlightAverageData> findDestinations(FlightQuery.Params filter) throws BaseException {
        this.validateApiCodes(CodeType.AIRLINE, filter.getAirlineList());
        this.validateApiCodes(CodeType.LOCATION, filter.getDestinationList());

        List<String> destinationList = filter.getDestinationList();
        List<FlightData> direction1FlightList = this.retrieveFlightData(destinationList.get(0), destinationList.get(1), filter);
        List<FlightData> direction2FlightList = this.retrieveFlightData(destinationList.get(1), destinationList.get(0), filter);

        Map<String, List<FlightData>> destinationMap = Stream.concat(
            Stream.ofNullable(direction1FlightList).flatMap(Collection::stream),
            Stream.ofNullable(direction2FlightList).flatMap(Collection::stream)
        ).collect(Collectors.groupingBy(FlightData::getDestination));

        Map<String, FlightAverageData> averageResult = new HashMap<>();
        destinationMap.forEach((key, value) -> {
            String airport = this.remotePort.findLocation(key).map(Location::getName).orElse(null);
            averageResult.put(key, this.mapper.toFlightAverageData(filter, airport, value));
        });

        return averageResult;
    }

    private void validateApiCodes(CodeType codeType, List<String> codeList) throws BaseException {
        ApiCodeValidator validator = this.validatorFactory.getValidator(codeType);
        validator.validate(codeList);
    }

    private List<FlightData> retrieveFlightData(String from, String to, FlightQuery.Params filter) {
        FlightRemotePort.Params params = this.mapper.toFlightSearchRemotePortParams(from, to, filter);
        return this.remotePort.findFlights(params);
    }
}
