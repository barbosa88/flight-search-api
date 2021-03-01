package pt.flightin.flightsearch.audit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pt.flightin.flightsearch.audit.document.RequestDocument;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.audit.mapper.RequestMapper;
import pt.flightin.flightsearch.audit.repository.RequestRepository;
import pt.flightin.flightsearch.core.exception.BaseException;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

class MongoAuditServiceTest {

    @Mock
    RequestRepository repository;
    @Mock
    RequestMapper mapper;
    @InjectMocks
    MongoAuditService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ValidInput_CallDependenciesAndAssertResult() {
        Mockito.when(this.repository.findAll())
               .thenReturn(List.of(Mockito.mock(RequestDocument.class)));

        Mockito.when(this.mapper.toRequestList(ArgumentMatchers.any()))
               .thenReturn(List.of(Mockito.mock(Request.class)));

        List<Request> result = this.service.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());

        Mockito.verify(this.repository, Mockito.times(1))
               .findAll();

        Mockito.verify(this.mapper, Mockito.times(1))
               .toRequestList(ArgumentMatchers.anyList());
    }

    @Test
    void findOne_ExistingResource_AssertResultIsPresent() throws BaseException {
        Mockito.when(this.repository.findById(ArgumentMatchers.anyString()))
               .thenReturn(Optional.of(Mockito.mock(RequestDocument.class)));

        Mockito.when(this.mapper.toRequest(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(Request.class));

        Request result = this.service.findOne("id");

        Assertions.assertNotNull(result);
    }

    @Test
    void findOne_NonExistingResource_ThrowsResourceNotFoundException() {
        Mockito.when(this.repository.findById(ArgumentMatchers.anyString()))
               .thenReturn(Optional.empty());

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.service.findOne("id")
        );
    }

    @Test
    void save_ValidInput_CallDependencies() {
        Mockito.when(this.mapper.toRequestDocument(ArgumentMatchers.any()))
               .thenReturn(Mockito.mock(RequestDocument.class));

        this.service.save(Mockito.mock(Request.class));

        Mockito.verify(this.repository, Mockito.times(1))
               .insert(ArgumentMatchers.any(RequestDocument.class));
    }

    @Test
    void delete_ExistingResource_CallDependencies() throws BaseException {
        Mockito.when(this.repository.existsById(ArgumentMatchers.anyString()))
               .thenReturn(true);

        this.service.delete("id");

        Mockito.verify(this.repository, Mockito.times(1))
               .existsById(ArgumentMatchers.anyString());

        Mockito.verify(this.repository, Mockito.times(1))
               .deleteById(ArgumentMatchers.anyString());
    }

    @Test
    void delete_NonExistingResource_ThrowsResourceNotFoundException() {
        Mockito.when(this.repository.existsById(ArgumentMatchers.anyString()))
               .thenReturn(false);

        Assertions.assertThrows(
            ResourceNotFoundException.class,
            () -> this.service.delete("id")
        );
    }
}