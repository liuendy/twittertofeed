package uk.co.landbit.twittertofeed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;

@Configuration
@EnableConfigurationProperties
@EnableSocial
public class ApplicationConfiguration {

	private @Value("${spring.social.twitter.app-id}") String consumerKey;
	private @Value("${spring.social.twitter.app-secret}") String consumerSecret;

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

}
