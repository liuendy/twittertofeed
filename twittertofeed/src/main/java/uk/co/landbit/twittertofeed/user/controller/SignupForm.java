package uk.co.landbit.twittertofeed.user.controller;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import uk.co.landbit.twittertofeed.user.domain.Role;

public class SignupForm {

    @NotEmpty
    private String username;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email = "";

    // @Size(min = 6, message = "must be at least 6 characters")
    @NotEmpty
    private String password = "";

    @NotNull
    private Role role = Role.ROLE_USER;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

}
