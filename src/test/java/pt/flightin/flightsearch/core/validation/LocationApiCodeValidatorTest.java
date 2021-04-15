package pt.flightin.flightsearch.core.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;

@SpringBootTest(classes = {LocationApiCodeValidator.class, ValidationAutoConfiguration.class})
class LocationApiCodeValidatorTest {

    @MockBean
    FlightRemotePort remotePort;
    @Autowired
    LocationApiCodeValidator validator;

    @Test
    void validate_ValidLocations_DoesNothing() throws BaseException {
        Mockito.when(this.remotePort.findLocation(ArgumentMatchers.anyString()))
               .thenReturn(Optional.of(Mockito.mock(Location.class)));

        this.validator.validate(List.of("OPO", "LIS"));
    }

    @Test
    void validate_InvalidLocations_ThrowsResourceNotFoundException() {
        Mockito.when(this.remotePort.findLocation(ArgumentMatchers.anyString()))
               .thenReturn(Optional.empty());

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.validator.validate(List.of("INVALID LOCATION"))
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