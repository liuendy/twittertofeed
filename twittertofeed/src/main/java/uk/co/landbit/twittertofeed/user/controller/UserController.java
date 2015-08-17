package uk.co.landbit.twittertofeed.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

@Controller
public class UserController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    
    protected static final String VIEW_NAME_REGISTRATION_PAGE = "user/signup";
    protected static final String MODEL_NAME_REGISTRATION_DTO = "signupForm";
    
    @RequestMapping(value = "/user/signup", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        LOGGER.debug("Rendering registration page.");

//        ProviderSignInUtils providerSignInUtils = new ProviderSignInUtils();
//        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

        SignupForm signUpForm = new SignupForm();
        model.addAttribute(MODEL_NAME_REGISTRATION_DTO, signUpForm);
        
        return VIEW_NAME_REGISTRATION_PAGE;
    }

}
