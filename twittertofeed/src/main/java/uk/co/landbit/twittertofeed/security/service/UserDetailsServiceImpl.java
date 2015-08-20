package uk.co.landbit.twittertofeed.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import uk.co.landbit.twittertofeed.security.domain.UserDetailsImpl;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;


/**
 * Spring security component
 *  
 * Implements loadUserByUsername 
 * -> username is the key, depends on your model, here the value of username is an email address
 * 
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
	this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	LOGGER.debug("Loading user by email : {}", email);

	User user = repository.findByEmail(email)
					.orElseThrow(()
					-> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));;
	
	LOGGER.debug("Found user: {}", user);

	UserDetailsImpl principal = 
		UserDetailsImpl.getBuilder()
                      	 .firstName(user.getFirstName())
                      	 .id(user.getId())
                       	 .lastName(user.getLastName())
                       	 .password(user.getPassword())
                       	 .role(user.getRole())
                       	 .socialSignInProvider(user.getSignInProvider())
                       	 .username(user.getEmail())
                       	 .build();

	LOGGER.debug("Returning user details: {}", principal);

	return principal;
    }
}