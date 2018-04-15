package ee.bilal.dev.engine.webscraper.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties", encoding="UTF-8")
public class ApplicationConfig {
    @Getter
    @Value("${core-pool-size}")
    private int corePoolSize;

    @Getter
    @Value("${max-pool-size}")
    private int maxPoolSize;

    @Getter
    @Value("${queue-capacity}")
    private int queueCapacity;
}
