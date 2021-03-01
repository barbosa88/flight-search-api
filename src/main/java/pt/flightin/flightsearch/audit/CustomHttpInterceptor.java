package pt.flightin.flightsearch.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pt.flightin.flightsearch.audit.domain.Request;
import pt.flightin.flightsearch.audit.mapper.RequestMapper;
import pt.flightin.flightsearch.audit.service.AuditService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CustomHttpInterceptor implements HandlerInterceptor {

    static final String REQUEST = "%s %s?%s";
    final AuditService auditService;
    final RequestMapper requestMapper;

    @Autowired
    public CustomHttpInterceptor(AuditService auditService,
        RequestMapper requestMapper) {
        this.auditService = auditService;
        this.requestMapper = requestMapper;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable Exception ex) throws JsonProcessingException {
        log.debug("HttpServletRequest processed! Forwarding to audit DB!");

        String headers = new ObjectMapper().writeValueAsString(this.retrieveHeaders(request));
        String req = String.format(REQUEST, request.getMethod(), request.getRequestURI(), request.getQueryString());
        Request requestHistory = this.requestMapper.toRequest(req, headers);

        this.auditService.save(requestHistory);
    }

    private Map<String, String> retrieveHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                          .collect(Collectors.toMap(h -> h, request::getHeader));
    }
}
