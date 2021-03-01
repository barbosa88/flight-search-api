package pt.flightin.flightsearch.skypicker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Airline {

    @JsonProperty("id")
    String code;
    String name;
}
