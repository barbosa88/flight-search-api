package pt.flightin.flightsearch.audit.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.flightin.flightsearch.audit.document.RequestDocument;
import pt.flightin.flightsearch.audit.domain.Request;

import java.util.List;

@Mapper
public interface RequestMapper {

    List<Request> toRequestList(List<RequestDocument> requestDocumentList);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "request", source = "request")
    @Mapping(target = "headers", source = "headers")
    Request toRequest(RequestDocument requestDocument);

    Request toRequest(String request, String headers);

    @InheritInverseConfiguration
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    RequestDocument toRequestDocument(Request request);

}
