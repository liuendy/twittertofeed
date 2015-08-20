package uk.co.landbit.twittertofeed.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * 
 * Spring social component
 *  
 * Implements loadUserByUserId 
 * -> userId is the key, depends on your model, here the value of userId is an email address
 * 
 */
public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialUserDetailsServiceImpl.class);

    private UserDetailsService userDetailsService;

    public SocialUserDetailsServiceImpl(UserDetailsService userDetailsService) {
	this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
	LOGGER.debug("Loading user by user id: {}", userId);

	UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

	LOGGER.debug("Found user details: {} for userId {}", userDetails, userId);

	return (SocialUserDetails) userDetails;
    }
}
