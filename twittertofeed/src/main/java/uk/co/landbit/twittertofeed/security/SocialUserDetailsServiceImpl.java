package uk.co.landbit.twittertofeed.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialUserDetailsServiceImpl.class);

    private UserDetailsService userDetailsService;

    public SocialUserDetailsServiceImpl(UserDetailsService userDetailsService) {
	this.userDetailsService = userDetailsService;
    }

    /**
     * Loads the username by using the account ID of the user.
     * 
     * @param userId The account ID of the requested user.
     * @return The information of the requested user.
     * @throws UsernameNotFoundException Thrown if no user is found.
     * @throws DataAccessException
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
	LOGGER.debug("Loading user by user id: {}", userId);

	UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
	LOGGER.debug("Found user details: {} for userId {}", userDetails, userId);

	return (SocialUserDetails) userDetails;
    }
}
