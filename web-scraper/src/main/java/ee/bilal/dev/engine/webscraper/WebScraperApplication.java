package ee.bilal.dev.engine.webscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class WebScraperApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebScraperApplication.class, args);
	}
}
