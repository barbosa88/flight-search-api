package pt.flightin.flightsearch.skypicker.client.errorhandling;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pt.flightin.flightsearch.core.exception.OutboundInvocationException;

@Slf4j
@Component
public class SkypickerErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        log.info("Error while invoking {}", response.request().url());

        String path = response.request().requestTemplate().path();
        return new OutboundInvocationException(String.format("Received %s from %s", response.reason(), path));
    }
}
