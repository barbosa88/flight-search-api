package pt.flightin.flightsearch.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.List;

@Slf4j
@Component("airlineValidator")
public class AirlineApiCodeValidator implements ApiCodeValidator {

    final FlightRemotePort remotePort;

    @Autowired
    public AirlineApiCodeValidator(FlightRemotePort remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public void validate(List<String> airlineList) throws BaseException {
        log.debug("Validating {} airlines", airlineList.toString());

        for (String code : airlineList) {
            this.remotePort.findAirline(code)
                           .orElseThrow(() -> new ResourceNotFoundException(code));
        }
    }
}
