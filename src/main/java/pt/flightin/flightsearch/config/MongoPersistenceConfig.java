package pt.flightin.flightsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "mongoAuditorProvider")
public class MongoPersistenceConfig {

    @Bean
    public AuditorAware<String> mongoAuditorProvider() {
        return new MongoAuditorAware();
    }
}
