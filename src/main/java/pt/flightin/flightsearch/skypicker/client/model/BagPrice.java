package pt.flightin.flightsearch.skypicker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BagPrice {

    @JsonProperty("1")
    BigDecimal bag1Price;
    @JsonProperty("2")
    BigDecimal bag2Price;
}
