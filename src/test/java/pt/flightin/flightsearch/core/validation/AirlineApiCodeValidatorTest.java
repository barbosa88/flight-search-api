package pt.flightin.flightsearch.core.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;

@SpringBootTest(classes = {AirlineApiCodeValidator.class, ValidationAutoConfiguration.class})
class AirlineApiCodeValidatorTest {

    @MockBean
    FlightRemotePort remotePort;
    @Autowired
    AirlineApiCodeValidator validator;

    @Test
    void validate_ValidAirlines_DoesNothing() throws BaseException {
        Mockito.when(this.remotePort.findAirline(ArgumentMatchers.anyString()))
               .thenReturn(Optional.of(Mockito.mock(Airline.class)));

        this.validator.validate(List.of("TP", "FR"));
    }

    @Test
    void validate_InvalidAirlines_ThrowsResourceNotFoundException() {
        Mockito.when(this.remotePort.findAirline(ArgumentMatchers.anyString()))
               .thenReturn(Optional.empty());

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.validator.validate(List.of("INVALID AIRLINE"))
        );
    }

    @Test
    void validate_NullListAsInput_ThrowsConstraintViolationException() {
        ConstraintViolationException violation = Assertions.assertThrows(
            ConstraintViolationException.class,
            () -> this.validator.validate(null)
        );

        Assertions.assertEquals(1, violation.getConstraintViolations().size());
        Assertions.assertEquals("validate.codeList: must not be empty", violation.getMessage());
    }

    @Test
    void validate_EmptyListAsInput_ThrowsConstraintViolationException() {
        ConstraintViolationException violation = Assertions.assertThrows(
            ConstraintViolationException.class,
            () -> this.validator.validate(Collections.emptyList())
        );

        Assertions.assertEquals(1, violation.getConstraintViolations().size());
        Assertions.assertEquals("validate.codeList: must not be empty", violation.getMessage());
    }
}