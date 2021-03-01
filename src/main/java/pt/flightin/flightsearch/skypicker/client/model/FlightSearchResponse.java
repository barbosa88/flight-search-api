package pt.flightin.flightsearch.skypicker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FlightSearchResponse {

    @JsonProperty("search_id")
    String searchId;
    String currency;
    @JsonProperty("data")
    List<FlightData> flightDataList;
}
