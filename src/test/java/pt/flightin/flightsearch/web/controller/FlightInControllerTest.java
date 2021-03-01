package pt.flightin.flightsearch.web.controller;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import org.springframework.util.MultiValueMap;
import pt.flightin.flightsearch.core.domain.FlightAverageData;
import pt.flightin.flightsearch.core.port.in.FlightQuery;
import pt.flightin.flightsearch.web.errorhandling.CustomExceptionHandler;
import pt.flightin.flightsearch.web.mapper.FlightInMapper;

import java.util.Collections;
import java.util.Map;

@WebMvcTest(controllers = FlightInController.class)
@ContextConfiguration(classes = FlightInController.class)
@ImportAutoConfiguration(CustomExceptionHandler.class)
class FlightInControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    FlightQuery query;
    @MockBean
    FlightInMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findDestinations_MockedEmptyResponse_ReturnsCode200AndEmptyResult() throws Exception {
        BDDMockito.given(this.mapper.toFlightSearchQueryParams(ArgumentMatchers.any(MultiValueMap.class)))
                  .willReturn(Mockito.mock(FlightQuery.Params.class));
        BDDMockito.given(this.query.findDestinations(ArgumentMatchers.any(FlightQuery.Params.class)))
                  .willReturn(Collections.emptyMap());

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI)
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void findDestinations_MockedResponse_ReturnsCode200AndResult() throws Exception {
        Map<String, FlightAverageData> mockResult = Map.ofEntries(
            Map.entry("OPO", FlightAverageData.builder().build())
        );

        BDDMockito.given(this.mapper.toFlightSearchQueryParams(ArgumentMatchers.any(MultiValueMap.class)))
                  .willReturn(Mockito.mock(FlightQuery.Params.class));
        BDDMockito.given(this.query.findDestinations(ArgumentMatchers.any(FlightQuery.Params.class)))
                  .willReturn(mockResult);

        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI)
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isMap())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void findDestinations_NotAcceptableContentTypeHeader_ReturnsCode406() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(Constants.URI)
                                                   .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @UtilityClass
    class Constants {

        static String URI = "/flights/avg";
    }
}