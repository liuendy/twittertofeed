package uk.co.landbit.twittertofeed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;

@Configuration
@EnableSocial
public class SocialConfig {

    /*
     * TODO try to remove and see if it's handled by auto config
     * handle the magic behind providers  connection flow (/connect/twitter...)
     */
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
	    ConnectionRepository connectionRepository) {
	return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

}