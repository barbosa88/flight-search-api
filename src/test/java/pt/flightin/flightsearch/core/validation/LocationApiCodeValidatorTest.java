package pt.flightin.flightsearch.core.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.flightin.flightsearch.core.domain.Location;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.List;
import java.util.Optional;

class LocationApiCodeValidatorTest {

    @Mock
    FlightRemotePort remotePort;
    @InjectMocks
    LocationApiCodeValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ValidLocations_DoesNothing() throws BaseException {
        Mockito.when(this.remotePort.findLocation(ArgumentMatchers.anyString()))
               .thenReturn(Optional.of(Mockito.mock(Location.class)));

        this.validator.validate(List.of("OPO", "LIS"));
    }

    @Test
    void validate_InvalidLocations_ThrowsResourceNotFoundException() throws BaseException {
        Mockito.when(this.remotePort.findLocation(ArgumentMatchers.anyString()))
               .thenReturn(Optional.empty());

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.validator.validate(List.of("INVALID LOCATION"))
        );
    }
}