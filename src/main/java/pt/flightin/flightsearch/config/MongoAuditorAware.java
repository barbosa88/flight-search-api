package pt.flightin.flightsearch.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class MongoAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("admin");
    }
}
