package pt.flightin.flightsearch.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.audit.service.AuditService;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.web.api.AuditApi;

import java.util.List;

@RestController
public class AuditController implements AuditApi {

    final AuditService service;

    @Autowired
    public AuditController(AuditService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<List<Request>> findAllRequests() {
        List<Request> requestList = this.service.findAll();
        return ResponseEntity.ok(requestList);
    }

    @Override
    public ResponseEntity<Request> findRequest(String id) throws BaseException {
        Request request = this.service.findOne(id);
        return ResponseEntity.ok(request);
    }

    @Override
    public ResponseEntity<Void> deleteRequest(String id) throws BaseException {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
