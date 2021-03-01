package pt.flightin.flightsearch.core.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.flightin.flightsearch.core.domain.CodeType;

import java.util.Map;

@Slf4j
@Component
public class ApiCodeValidatorFactory {

    final Map<String, ApiCodeValidator> strategyMap;

    @Autowired
    public ApiCodeValidatorFactory(Map<String, ApiCodeValidator> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public ApiCodeValidator getValidator(CodeType codeType) {
        log.debug("Retrieving API validator strategy for {}", codeType);

        switch (codeType) {
            case AIRLINE:
                return this.strategyMap.get("airlineValidator");
            case LOCATION:
                return this.strategyMap.get("locationValidator");
            default:
                throw new IllegalArgumentException("Invalid codeType");
        }
    }
}
