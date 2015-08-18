package uk.co.landbit.twittertofeed.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import uk.co.landbit.twittertofeed.TwitterToFeedApplication;

public class ServletInitializer extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(TwitterToFeedApplication.class);
    }
}