package uk.co.landbit.twittertofeed.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter { //implements SocialConfigurer {

    @Autowired
    private DataSource dataSource;

    // @Bean
    // public ConnectController connectController(ConnectionFactoryLocator
    // connectionFactoryLocator,
    // ConnectionRepository connectionRepository) {
    // return new ConnectController(connectionFactoryLocator,
    // connectionRepository);
    // }

    // TODO change encryptor
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
	return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }

    @Override
    public UserIdSource getUserIdSource() {
	return new UserIdSource() {
	    @Override
	    public String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
		    throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return authentication.getName();
	    }
	};
    }

//    @Override
//    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
//	    Environment environment) {
//	connectionFactoryConfigurer.addConnectionFactory(
//		new TwitterConnectionFactory(
//			environment.getProperty("spring.social.twitter.app-id"),
//			environment.getProperty("spring.social.twitter.app-secret")
//			));
//
//    }

//    //
//    // API Binding Beans
//    //
//
//    @Bean
//    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
//    public Twitter twitter(ConnectionRepository repository) {
//	Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
//	return connection != null ? connection.getApi() : null;
//    }
//
//    //
//    // Web Controller and Filter Beans
//    //
//    @Bean
//    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
//	    ConnectionRepository connectionRepository) {
//	ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
//	// connectController.addInterceptor(new TweetAfterConnectInterceptor());
//	return connectController;
//    }

    // @Bean
    // public ProviderSignInController
    // providerSignInController(ConnectionFactoryLocator
    // connectionFactoryLocator, UsersConnectionRepository
    // usersConnectionRepository) {
    // return new ProviderSignInController(connectionFactoryLocator,
    // usersConnectionRepository, new SimpleSignInAdapter(new
    // HttpSessionRequestCache()));
    // }
    //

//    @Bean
//    public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository,
//	    UserIdSource userIdSource) {
//	return new ReconnectFilter(usersConnectionRepository, userIdSource);
//    }

}