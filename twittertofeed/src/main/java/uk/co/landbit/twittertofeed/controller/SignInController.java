package uk.co.landbit.twittertofeed.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignInController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInController.class);

    protected static final String VIEW_NAME_LOGIN_PAGE = "user/signin";

    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String showLoginPage() {
	LOGGER.debug("Rendering sign in page.");
	return VIEW_NAME_LOGIN_PAGE;
    }
}
