package uk.co.landbit.twittertofeed.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.SocialUser;

import uk.co.landbit.twittertofeed.user.domain.Role;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;

//TODO 
public class SocialUserDetails extends SocialUser {
    
//    private String password;
//    private final String username;
//    private final Set<GrantedAuthority> authorities;
//    private final boolean accountNonExpired;
//    private final boolean accountNonLocked;
//    private final boolean credentialsNonExpired;
//    private final boolean enabled;

    private Long id;

    private String firstName;

    private String lastName;

    private Role role;

    private SignInProvider signInProvider;

    public SocialUserDetails(String username, String password,
	    Collection<? extends GrantedAuthority> authorities) {
	super(username, password, authorities);
	// TODO Auto-generated constructor stub
    }

}
