package pt.flightin.flightsearch.audit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.flightin.flightsearch.audit.document.RequestDocument;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.audit.mapper.RequestMapper;
import pt.flightin.flightsearch.audit.repository.RequestRepository;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;

import java.util.List;

@Slf4j
@Service
public class MongoAuditService implements AuditService {

    final RequestRepository repository;
    final RequestMapper mapper;

    @Autowired
    public MongoAuditService(RequestRepository repository, RequestMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Request> findAll() {
        log.debug("Retrieving request history from audit DB");

        List<RequestDocument> requestDocumentList = this.repository.findAll();
        return this.mapper.toRequestList(requestDocumentList);
    }

    @Override
    public Request findOne(String id) throws BaseException {
        log.debug("Retrieving request with id:{} from audit DB", id);

        RequestDocument requestDocument = this.repository.findById(id)
                                                         .orElseThrow(() -> new ResourceNotFoundException(id));
        return this.mapper.toRequest(requestDocument);
    }

    @Override
    public void save(Request request) {
        log.debug("Saving {} to audit DB", request);

        RequestDocument requestDocument = this.mapper.toRequestDocument(request);
        this.repository.insert(requestDocument);
    }

    @Override
    public void delete(String id) throws BaseException {
        log.debug("Deleting request with id:{} from audit DB", id);

        if (!this.repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        this.repository.deleteById(id);
    }
}
