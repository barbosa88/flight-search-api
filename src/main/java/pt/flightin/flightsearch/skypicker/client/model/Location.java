package pt.flightin.flightsearch.skypicker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Location {

    String code;
    @JsonProperty("alternative_names")
    List<String> nameList;
}
