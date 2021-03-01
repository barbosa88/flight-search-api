package pt.flightin.flightsearch.core.port.in;

import lombok.Builder;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.exception.BaseException;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Validated
public interface FlightQuery {

    Map<String, FlightAverageData> findDestinations(@Valid FlightQuery.Params filter) throws BaseException;

    @Value
    @Builder
    class Params {

        @NotEmpty
        @Size(min = 2, max = 2)
        List<String> destinationList;
        @NotEmpty
        @Size(min = 1)
        List<String> airlineList;
        @NotBlank
        String currency;
        String dateFrom;
        String dateTo;
    }
}
