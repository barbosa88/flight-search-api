package pt.flightin.flightsearch.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

    String code;
    String name;
}
