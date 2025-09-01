package bag.livestock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AsyncConfig implements WebMvcConfigurer {

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // Set timeout to 1 hour (3600000 milliseconds)
        // Use -1 for no timeout
        configurer.setDefaultTimeout(3600000);
    }
}
