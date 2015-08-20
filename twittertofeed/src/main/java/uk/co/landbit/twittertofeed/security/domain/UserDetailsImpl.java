package uk.co.landbit.twittertofeed.security.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;

import uk.co.landbit.twittertofeed.user.domain.Role;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;

// TODO review builder
public class UserDetailsImpl extends SocialUser {

    private static final long serialVersionUID = 1L;

//TODO
//    private User user;
//    private List<GrantedAuthority> roles;
    
    private Long id;

    private String firstName;

    private String lastName;

    private Role role;

    private SignInProvider signInProvider;

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
	super(username, password, authorities);
    }

    public static Builder getBuilder() {
	return new Builder();
    }

    public Long getId() {
	return id;
    }

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public Role getRole() {
	return role;
    }

    public static class Builder {

	private Long id;

	private String username;

	private String firstName;

	private String lastName;

	private String password;

	private Role role;

	private SignInProvider signInProvider;

	private Set<GrantedAuthority> authorities;

	public Builder() {
	    this.authorities = new HashSet<>();
	}

	public Builder firstName(String firstName) {
	    this.firstName = firstName;
	    return this;
	}

	public Builder id(Long id) {
	    this.id = id;
	    return this;
	}

	public Builder lastName(String lastName) {
	    this.lastName = lastName;
	    return this;
	}

	public Builder password(String password) {
	    if (password == null) {
		password = "SocialUser";
	    }

	    this.password = password;
	    return this;
	}

	public Builder role(Role role) {
	    this.role = role;

	    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
	    this.authorities.add(authority);

	    return this;
	}

	public Builder socialSignInProvider(SignInProvider signInProvider) {
	    this.signInProvider = signInProvider;
	    return this;
	}

	public Builder username(String username) {
	    this.username = username;
	    return this;
	}

	public UserDetailsImpl build() {
	    UserDetailsImpl user = new UserDetailsImpl(username, password, authorities);

	    user.id = id;
	    user.firstName = firstName;
	    user.lastName = lastName;
	    user.role = role;
	    user.signInProvider = signInProvider;

	    return user;
	}
    }
}
