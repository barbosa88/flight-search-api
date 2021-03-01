package pt.flightin.flightsearch.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FlightAverageData {

    @JsonProperty("name")
    String airport;
    String currency;
    @JsonProperty("from")
    @Schema(type = "string", format = "date")
    String dateFrom;
    @JsonProperty("to")
    @Schema(type = "string", format = "date")
    String dateTo;
    @JsonProperty("price_average")
    BigDecimal priceAverage;
    @JsonProperty("bags_price")
    BagAveragePrice bagAverage;
}
