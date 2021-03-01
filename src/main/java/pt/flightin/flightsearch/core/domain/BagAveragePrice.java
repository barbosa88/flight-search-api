package pt.flightin.flightsearch.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BagAveragePrice {

    @JsonProperty("bag1_average")
    BigDecimal bag1Average;
    @JsonProperty("bag2_average")
    BigDecimal bag2Average;
}
