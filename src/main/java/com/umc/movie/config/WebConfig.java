package com.umc.movie.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class WebConfig implements WebMvcConfigurer{
 @Value("${app.cors.enabled:true}") private boolean enabled;
 @Value("${app.cors.origin:http://localhost:5173}") private String origin;
 @Override public void addCorsMappings(CorsRegistry registry){
  if(enabled) registry.addMapping("/**").allowedOrigins(origin).allowedMethods("GET","POST","PATCH","DELETE").allowedHeaders("*");
 }
}
