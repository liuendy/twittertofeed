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

import io.undertow.security.idm.Account;
import uk.co.landbit.twittertofeed.security.SignInUtils;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Controller
public class UserSignUpController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSignUpController.class);

    protected static final String VIEW_NAME_REGISTRATION_PAGE = "user/signup";
    protected static final String MODEL_NAME_REGISTRATION_DTO = "signupForm";

    @Autowired
    private UserRepository accountRepository;
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    public UserSignUpController(UserRepository accountRepository) {
	this.accountRepository = accountRepository;
	this.providerSignInUtils = new ProviderSignInUtils();
    }

    // @RequestMapping(value = "/user/signup", method = RequestMethod.GET)
    // public String showRegistrationForm(WebRequest request, Model model) {
    // LOGGER.debug("Rendering registration page.");
    //
    // // ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
    // // Connection<?> connection =
    // // providerSignInUtils.getConnectionFromSession(request);
    //
    // SignupForm signUpForm = new SignupForm();
    // model.addAttribute(MODEL_NAME_REGISTRATION_DTO, signUpForm);
    //
    // return VIEW_NAME_REGISTRATION_PAGE;
    // }

    @RequestMapping(value = "/user/signup", method = RequestMethod.GET)
    public SignupForm signupForm(WebRequest request) {
	Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

	if (connection != null) {
	    return preFillSignUpForm(connection);
	} else {
	    return new SignupForm();
	}

    }
    
    
    @RequestMapping(value="/user/signup", method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		User account = createAccount(form, formBinding);
		if (account != null) {
			SignInUtils.signin(account);
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

    private User createAccount(SignupForm form, BindingResult formBinding) {
	//try {
	    // TODO proper impl, add ROLE_USER
	    User account = new User(form.getEmail(), form.getPassword(), form.getFirstName(),
		    form.getLastName());
	    accountRepository.save(account);
	    return account;
//	} catch (UsernameAlreadyInUseException e) {
//	    formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
//	    return null;
//	}
    }
}
