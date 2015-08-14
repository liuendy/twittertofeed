package uk.co.landbit.twittertofeed.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUser;

public class TwitterToFeedUserDetails extends SocialUser {

    public TwitterToFeedUserDetails(String username, String password,
	    Collection<? extends GrantedAuthority> authorities) {
	super(username, password, authorities);
	// TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

}
