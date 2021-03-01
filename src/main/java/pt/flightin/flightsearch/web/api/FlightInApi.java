package pt.flightin.flightsearch.web.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.exception.BaseException;

import java.util.Map;

@RequestMapping(path = "/flights", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "flightin-search")
public interface FlightInApi {

    @GetMapping("/avg")
    @Parameter(name = "dest", in = ParameterIn.QUERY, array = @ArraySchema(schema = @Schema(type = "string")), required = true)
    @Parameter(name = "airline", in = ParameterIn.QUERY, array = @ArraySchema(schema = @Schema(type = "string")), required = true)
    @Parameter(name = "curr", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
    @Parameter(name = "dateFrom", in = ParameterIn.QUERY, schema = @Schema(type = "string", format = "date"))
    @Parameter(name = "dateTo", in = ParameterIn.QUERY, schema = @Schema(type = "string", format = "date"))
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, FlightAverageData>> findDestinations(@RequestParam MultiValueMap<String, String> queryString)
        throws BaseException;
}
