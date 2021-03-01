package pt.flightin.flightsearch.core.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.flightin.flightsearch.core.domain.Airline;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.core.port.out.FlightRemotePort;

import java.util.List;
import java.util.Optional;

class AirlineApiCodeValidatorTest {

    @Mock
    FlightRemotePort remotePort;
    @InjectMocks
    AirlineApiCodeValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ValidAirlines_DoesNothing() throws BaseException {
        Mockito.when(this.remotePort.findAirline(ArgumentMatchers.anyString()))
               .thenReturn(Optional.of(Mockito.mock(Airline.class)));

        this.validator.validate(List.of("TP", "FR"));
    }

    @Test
    void validate_InvalidAirlines_ThrowsResourceNotFoundException() throws BaseException {
        Mockito.when(this.remotePort.findAirline(ArgumentMatchers.anyString()))
               .thenReturn(Optional.empty());

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.validator.validate(List.of("INVALID AIRLINE"))
        );
    }
}