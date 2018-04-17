package ee.bilal.dev.engine.webscraper.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by bilal90 on 16/04/2018.
 */
@Configuration
@ComponentScan
public class WebMvcConfiguration implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }

            @Override
            public void addInterceptors (InterceptorRegistry registry) {
                registry.addInterceptor(new LoggerInterceptor());
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry
                        .addResourceHandler("/resources/**")
                        .addResourceLocations("/resources/");

                super.addResourceHandlers(registry);
            }
        };
    }*/
}
