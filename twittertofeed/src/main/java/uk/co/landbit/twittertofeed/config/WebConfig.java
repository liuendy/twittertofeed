package uk.co.landbit.twittertofeed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// TODO Read http://www.webjars.org/documentation
// Making dependencies version agnostic
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	 registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	// servlet > 3
	//registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
    }

}