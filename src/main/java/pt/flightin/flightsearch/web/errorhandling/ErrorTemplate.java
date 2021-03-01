package pt.flightin.flightsearch.web.errorhandling;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ErrorTemplate {

    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
    String message;
    String path;

}
