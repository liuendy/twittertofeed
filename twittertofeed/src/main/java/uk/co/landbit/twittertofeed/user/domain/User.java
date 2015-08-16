package uk.co.landbit.twittertofeed.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "signInProvider", length = 32)
    private SignInProvider signInProvider;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
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
