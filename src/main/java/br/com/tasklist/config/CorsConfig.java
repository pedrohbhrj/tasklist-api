package br.com.tasklist.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry){
        registry
                .addMapping("/**")
                .allowedMethods("POST","GET","PATCH","PUT","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:5173/")
                .maxAge(3600);
    }
}
