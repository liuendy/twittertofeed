package uk.co.landbit.twittertofeed.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    

    protected static final String VIEW_NAME_HOMEPAGE = "index2";

    @RequestMapping(value = "/test/index2", method = RequestMethod.GET)
    public String showHomePage() {
	LOG.debug("Rendering index2 page");
	return "test/index2";
    }
   
}
