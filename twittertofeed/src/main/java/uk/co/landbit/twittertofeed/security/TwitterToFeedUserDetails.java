package uk.co.landbit.twittertofeed.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialUser;

import uk.co.landbit.twittertofeed.user.domain.Role;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;

//TODO 
public class TwitterToFeedUserDetails extends SocialUser {

    private Long id;

    private String firstName;

    private String lastName;

    private Role role;

    private SignInProvider signInProvider;

    public TwitterToFeedUserDetails(String username, String password,
	    Collection<? extends GrantedAuthority> authorities) {
	super(username, password, authorities);
	// TODO Auto-generated constructor stub
    }

}
