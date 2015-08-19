package uk.co.landbit.twittertofeed.user.controller;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import uk.co.landbit.twittertofeed.user.domain.Role;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;

public class SignupForm {

    // @NotEmpty
    private String firstName;

    // @NotEmpty
    private String lastName;

    @NotEmpty
    private String email = "";

    // @Size(min = 6, message = "must be at least 6 characters")
    //@NotEmpty
    private String password = "";

    //@NotNull
    private Role role = Role.ROLE_USER;

    private SignInProvider signInProvider;

    public boolean isSocialSignIn() {
        return signInProvider != null;
    }
    
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Role getRole() {
	return role;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public SignInProvider getSignInProvider() {
	return signInProvider;
    }

    public void setSignInProvider(SignInProvider signInProvider) {
	this.signInProvider = signInProvider;
    }

}
