package uk.co.landbit.twittertofeed.controller;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import uk.co.landbit.twittertofeed.user.domain.Role;

public class SignupForm {

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

    public void setRole(Role role) {
        this.role = role;
    }

}
