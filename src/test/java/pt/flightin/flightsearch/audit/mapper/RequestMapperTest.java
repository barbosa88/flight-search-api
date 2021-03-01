package pt.flightin.flightsearch.audit.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pt.flightin.flightsearch.audit.document.RequestDocument;
import pt.flightin.flightsearch.audit.domain.Request;

import java.time.LocalDateTime;
import java.util.List;

class RequestMapperTest {

    static RequestMapper mapper;

    @BeforeAll
    static void setUp() {
        mapper = Mappers.getMapper(RequestMapper.class);
    }

    @Test
    void toRequestList_ValidInput_IsProperlyMapped() {
        LocalDateTime now = LocalDateTime.now();
        List<RequestDocument> requestDocumentList = List.of(
            this.buildRequestDocument(now, "1", "admin", "req1", "headers1"),
            this.buildRequestDocument(now, "2", "user", "req2", "headers2")
        );

        List<Request> expected = List.of(
            this.buildRequest(now, "1", "admin", "req1", "headers1"),
            this.buildRequest(now, "2", "user", "req2", "headers2")
        );
        List<Request> result = mapper.toRequestList(requestDocumentList);

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toRequestList(null));
    }

    private RequestDocument buildRequestDocument(LocalDateTime createdDate, String id, String admin, String req, String headers) {
        return RequestDocument.builder().id(id).createdBy(admin).createdDate(createdDate).request(req).headers(headers).build();
    }

    private Request buildRequest(LocalDateTime createdDate, String id, String admin, String req, String headers) {
        return Request.builder().id(id).createdBy(admin).createdDate(createdDate).request(req).headers(headers).build();
    }

    @Test
    void toRequest_ValidInput_IsProperlyMapped() {
        Request expected = Request.builder().request("request").headers("headers").build();
        Request result = mapper.toRequest("request", "headers");

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toRequest(null, null));
    }

    @Test
    void toRequestDocument_ValidInput_IsProperlyMapped() {
        Request request = this.buildRequest(null, null, "admin", "req1", "headers1");

        RequestDocument expected = this.buildRequestDocument(null, null, "admin", "req1", "headers1");
        RequestDocument result = mapper.toRequestDocument(request);

        Assertions.assertEquals(expected, result);
        Assertions.assertNull(mapper.toRequest(null, null));
    }
}