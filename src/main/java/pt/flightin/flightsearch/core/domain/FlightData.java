package pt.flightin.flightsearch.core.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FlightData {

    String destination;
    String currency;
    BigDecimal price;
    BagPrice bagPrice;
}
