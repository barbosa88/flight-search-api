package pt.flightin.flightsearch.audit.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "requests")
@Data
@Builder
public class RequestDocument {

    @Id
    String id;
    @CreatedDate
    LocalDateTime createdDate;
    @CreatedBy
    String createdBy;
    String request;
    String headers;
}
