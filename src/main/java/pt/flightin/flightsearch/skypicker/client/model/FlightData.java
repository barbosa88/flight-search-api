package pt.flightin.flightsearch.skypicker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class FlightData {

    @JsonProperty("search_id")
    String searchId;
    @JsonProperty("flyTo")
    String destination;
    @JsonProperty("airlines")
    List<String> airlineList;
    BigDecimal price;
    @JsonProperty("bags_price")
    BagPrice bagPrice;

}
