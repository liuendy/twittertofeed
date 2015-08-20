package uk.co.landbit.twittertofeed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import uk.co.landbit.twittertofeed.security.service.SocialUserDetailsServiceImpl;
import uk.co.landbit.twittertofeed.security.service.UserDetailsServiceImpl;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void configure(WebSecurity web) throws Exception {

	web
		// TODO tidy
		// Spring Security ignores request to static resources.
		.ignoring()
			.antMatchers("/webjars/**")
			.antMatchers("/static/**")
			.antMatchers("/**/*.css", "/**/*.png", "/**/*.gif", "/**/*.jpg");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

	http
		// Configures form login
		.formLogin()
        		.loginPage("/signin")
        		.loginProcessingUrl("/signin/authenticate")
        		.failureUrl("/signin?param.error=bad_credentials")
		// Configures the logout function
		.and()
			.logout()
			.deleteCookies("JSESSIONID")
			.logoutUrl("/signout")
			//.logoutSuccessUrl("/login")
		// Configures url based authorization
		.and()
			.authorizeRequests()
				// Anyone can access the urls
				//.antMatchers("/auth/**", "/login", "/signup/**", "/user/register/**").permitAll()
				.antMatchers("/test/**").permitAll()
				.antMatchers("/admin/**", "/favicon.ico", "/resources/**", "/auth/**", "/signin/**", "/signup/**", "/disconnect/facebook", "/user/signup/**").permitAll()
				// The rest of the our application is protected.
				//.antMatchers("/**").hasRole("USER")
				.antMatchers("/**").authenticated()
		// Enabling provider sign in with SocialAuthenticationFilter
		// Compares connections (by user ID) if there is a match user is signed in
		// if no match, then user us sent to sign up page
		.and()
			.apply(new SpringSocialConfigurer())
		// TODO investigate remember me
		.and()
			.rememberMe();
    }

    /**
     * Configures the authentication manager bean which processes authentication
     * requests.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * This bean is load the user specific data when form login is used.
     */
    @Bean
    public UserDetailsService userDetailsService() {
	return new UserDetailsServiceImpl(userRepository);
    }

    /**
     * This is used to hash the password of the user.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder(10);
    }

    /**
     * This bean is used to load the user specific data when social sign in is
     * used.
     */
    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
	return new SocialUserDetailsServiceImpl(userDetailsService());
    }

}
