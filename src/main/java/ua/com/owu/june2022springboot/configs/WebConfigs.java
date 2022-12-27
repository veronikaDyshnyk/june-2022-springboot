package ua.com.owu.june2022springboot.configs;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@EnableWebMvc
@Configuration
public class WebConfigs implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String folder = System.getProperty("user.home")+ File.separator +"pictures"+File.separator;
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///"+ folder);
    }
}
