package uk.co.landbit.twittertofeed.security.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.co.landbit.twittertofeed.security.domain.UserAccountDetails;
import uk.co.landbit.twittertofeed.user.domain.User;

// TODO Send email to validate user account, do not auto sign in user after registration
public class SignInUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInUtils.class);

    /**
     * Programmatically signs in the user with the given the user ID.
     */
    public static void programmaticallySignIn(User user) {
	LOGGER.debug("Programmatically signing in user: {}", user);

	UserAccountDetails userDetails = UserAccountDetails.getBuilder().firstName(user.getFirstName()).id(user.getId())
		.lastName(user.getLastName()).password(user.getPassword()).role(user.getRole())
		.socialSignInProvider(user.getSignInProvider()).username(user.getEmail()).build();
	LOGGER.debug("Signing in principal: {}", userDetails);

	Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
		userDetails.getAuthorities());
	SecurityContextHolder.getContext().setAuthentication(authentication);
	LOGGER.info("User: {} has been signed in.", userDetails);
    }
}
