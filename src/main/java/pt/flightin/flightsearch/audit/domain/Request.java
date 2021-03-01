package pt.flightin.flightsearch.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Request {

    String id;
    LocalDateTime createdDate;
    String createdBy;
    String request;
    String headers;
}
