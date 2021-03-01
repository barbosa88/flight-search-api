package pt.flightin.flightsearch.web.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.core.exception.BaseException;

import java.util.List;

@RequestMapping(path = "/flights", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "flightin-history")
public interface AuditApi {

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<List<Request>> findAllRequests();

    @GetMapping("/history/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Request> findRequest(@PathVariable("id") String id) throws BaseException;

    @DeleteMapping("/history/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteRequest(@PathVariable("id") String id) throws BaseException;
}
