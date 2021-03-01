package pt.flightin.flightsearch.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pt.flightin.flightsearch.audit.document.RequestDocument;

@Repository
public interface RequestRepository extends MongoRepository<RequestDocument, String> {
}
