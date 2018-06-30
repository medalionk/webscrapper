package ee.bilal.dev.engine.webscraper.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

/**
 * Created by bilal90 on 10/26/2017.
 */
@Component
@Configuration
//@PropertySource(value = "classpath:application.properties", encoding="UTF-8")
public class ApplicationConfig {
    @Getter
    @Min(0)
    @Value("${pool-size}")
    private int poolSize;

    @Getter
    @Min(0)
    @Value("${max-pool-size}")
    private int maxPoolSize;

    @Getter
    @Min(0)
    @Value("${queue-capacity}")
    private int queueCapacity;
}
