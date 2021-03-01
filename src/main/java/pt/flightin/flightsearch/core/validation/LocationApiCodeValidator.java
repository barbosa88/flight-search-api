package pt.flightin.flightsearch.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.List;

@Slf4j
@Component("locationValidator")
public class LocationApiCodeValidator implements ApiCodeValidator {

    final FlightRemotePort remotePort;

    @Autowired
    public LocationApiCodeValidator(FlightRemotePort remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public void validate(List<String> locationList) throws BaseException {
        log.debug("Validating {} locations", locationList.toString());

        for (String code : locationList) {
            this.remotePort.findLocation(code)
                           .orElseThrow(() -> new ResourceNotFoundException(code));
        }
    }
}
