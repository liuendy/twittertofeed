package uk.co.landbit.twittertofeed.user.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import uk.co.landbit.twittertofeed.security.SignInUtils;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.service.UserService;

@Controller
public class UserSignUpController {

    private static final Logger LOG = LoggerFactory.getLogger(UserSignUpController.class);

    protected static final String VIEW_NAME_REGISTRATION_PAGE = "user/signup";
    protected static final String MODEL_NAME_REGISTRATION_DTO = "signupForm";

    
    private final UserService accountService;
    private final ProviderSignInUtils providerSignInUtils;

    @Autowired
    public UserSignUpController(UserService accountService) {
	this.accountService = accountService;
	this.providerSignInUtils = new ProviderSignInUtils();
    }

   
    @RequestMapping(value = "/user/signup", method = RequestMethod.GET)
    public SignupForm signupForm(WebRequest request) {
	Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

	if (connection != null) {
	    return preFillSignUpForm(connection);
	} else {
	    return new SignupForm();
	}

    }
    
    
    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
	if (formBinding.hasErrors()) {
	    LOG.debug("Validation errors found. Rendering form view.");
	    return null;
	}
	User account = createUserAccount(form, formBinding);

	LOG.debug("No validation errors found. Continuing registration process.");

	if (account != null) {
	    SignInUtils.signin(account);
	    LOG.debug("User {} has been signed in", account);

	    // If the user is signing in by using a social provider, this method
	    // call stores
	    // the connection to the UserConnection table. Otherwise, this
	    // method does not
	    // do anything.
	    providerSignInUtils.doPostSignUp(account.getEmail(), request);
	    return "redirect:/";
	}
	return null;
    }
    
    // internal helpers

    private SignupForm preFillSignUpForm(Connection<?> connection) {

	SignupForm accountForm = new SignupForm();

	UserProfile socialMediaProfile = connection.fetchUserProfile();
	accountForm.setEmail(socialMediaProfile.getEmail());
	accountForm.setFirstName(socialMediaProfile.getFirstName());
	accountForm.setLastName(socialMediaProfile.getLastName());
	accountForm.setSignInProvider(SignInProvider.valueOf(connection.getKey().getProviderId().toUpperCase()));

	return accountForm;
    }

//    private User createAccount(SignupForm form, BindingResult formBinding) {
//	//try {
//	    // TODO proper impl, add ROLE_USER
//	    User account = new User(form.getEmail(), form.getPassword(), form.getFirstName(),
//		    form.getLastName());
//	    
//	    accountService.signup(form);
//	    return account;
////	} catch (UsernameAlreadyInUseException e) {
////	    formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
////	    return null;
////	}
//    }
    
    private User createUserAccount(SignupForm form, BindingResult result) {
	LOG.debug("Creating user account with information: {}", form);
	User account = null;

//	try {
	    account = accountService.signup(form);
//	} catch (DuplicateEmailException ex) {
//	    LOGGER.debug("An email address: {} exists.", userAccountData.getEmail());
//	    addFieldError(MODEL_NAME_REGISTRATION_DTO, RegistrationForm.FIELD_NAME_EMAIL, userAccountData.getEmail(),
//		    ERROR_CODE_EMAIL_EXIST, result);
//	}

	return account;
    }

}
