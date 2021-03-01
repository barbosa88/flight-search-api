package pt.flightin.flightsearch.core.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pt.flightin.flightsearch.core.domain.CodeType;

import java.util.Map;

class ApiCodeValidatorFactoryTest {

    static ApiCodeValidatorFactory factory;

    @BeforeAll
    static void setUp() {
        factory = new ApiCodeValidatorFactory(buildStrategyMap());
    }

    private static Map<String, ApiCodeValidator> buildStrategyMap() {
        return Map.ofEntries(
            Map.entry("airlineValidator", Mockito.mock(AirlineApiCodeValidator.class)),
            Map.entry("locationValidator", Mockito.mock(LocationApiCodeValidator.class))
        );
    }

    @Test
    void getValidator_AirlineValidator_ReturnsProperStrategyInstance() {
        ApiCodeValidator result = factory.getValidator(CodeType.AIRLINE);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof AirlineApiCodeValidator);
    }

    @Test
    void getValidator_LocationValidator_ReturnsProperStrategyInstance() {
        ApiCodeValidator result = factory.getValidator(CodeType.LOCATION);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof LocationApiCodeValidator);
    }
}