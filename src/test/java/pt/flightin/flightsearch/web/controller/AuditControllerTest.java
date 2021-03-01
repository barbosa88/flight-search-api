package pt.flightin.flightsearch.web.controller;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.audit.service.AuditService;
import pt.flightin.flightsearch.core.exception.ResourceNotFoundException;
import pt.flightin.flightsearch.web.errorhandling.CustomExceptionHandler;

import java.util.Collections;
import java.util.List;

@WebMvcTest(controllers = AuditController.class)
@ContextConfiguration(classes = AuditController.class)
@ImportAutoConfiguration(CustomExceptionHandler.class)
class AuditControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AuditService service;
    @InjectMocks
    AuditController controller;

    @Test
    void findAllRequests_MockedEmptyResponse_ReturnsCode200AndEmptyResult() throws Exception {
        BDDMockito.given(this.service.findAll()).willReturn(Collections.emptyList());

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI)
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void findAllRequests_MockedResponse_ReturnsCode200AndResult() throws Exception {
        BDDMockito.given(this.service.findAll()).willReturn(List.of(Mockito.mock(Request.class)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI)
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void findRequest_MockedEmptyResponse_ReturnsCode404AndErrorMessage() throws Exception {
        BDDMockito.given(this.service.findOne(ArgumentMatchers.anyString()))
                  .willThrow(new ResourceNotFoundException("123"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI_WITH_ID, "123")
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("123 not found"));
    }

    @Test
    void findRequest_MockedResponse_ReturnsCode200AndResult() throws Exception {
        Request mockResponse = Request.builder().id("123").build();
        BDDMockito.given(this.service.findOne(ArgumentMatchers.anyString()))
                  .willReturn(mockResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI_WITH_ID, "123")
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void deleteRequest_MockedEmptyResponse_ReturnsCode404AndErrorMessage() throws Exception {
        BDDMockito.doThrow(new ResourceNotFoundException("123"))
                  .when(this.service).delete(ArgumentMatchers.anyString());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(Constants.URI_WITH_ID, "123")
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("123 not found"));
    }

    @Test
    void deleteRequest_ExistingRequest_ReturnsCode204() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(Constants.URI_WITH_ID, "123")
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @UtilityClass
    class Constants {

        static String URI = "/flights/history";
        static String URI_WITH_ID = URI.concat("/{id}");
    }
}