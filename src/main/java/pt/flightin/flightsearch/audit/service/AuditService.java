package pt.flightin.flightsearch.audit.service;

import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.core.exception.BaseException;

import java.util.List;

public interface AuditService {

    List<Request> findAll();

    Request findOne(String id) throws BaseException;

    void save(Request request);

    void delete(String id) throws BaseException;
}
