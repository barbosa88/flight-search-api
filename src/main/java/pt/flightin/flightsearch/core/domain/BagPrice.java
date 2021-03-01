package pt.flightin.flightsearch.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BagPrice {

    BigDecimal bag1Price;
    BigDecimal bag2Price;
}
