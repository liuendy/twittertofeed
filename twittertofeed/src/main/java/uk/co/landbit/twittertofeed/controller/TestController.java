package uk.co.landbit.twittertofeed.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    private UsersConnectionRepository usersConnectionRepository;

    private UserRepository userRepository;

    @Autowired
    public TestController(UserRepository userRepository, UsersConnectionRepository usersConnectionRepository) {
	this.userRepository = userRepository;
	this.usersConnectionRepository = usersConnectionRepository;
    }

    @RequestMapping(value = "/api/timeline", method = { RequestMethod.GET }, produces = { "application/json" })
    public ResponseEntity<String> timeline(Model model) {

	List<User> users = userRepository.findBySignInProvider(SignInProvider.TWITTER);
	for (User u : users) {
	    ConnectionRepository connectionRepository = usersConnectionRepository
		    .createConnectionRepository(u.getEmail());
	    Connection<Twitter> twitterCon = connectionRepository.getPrimaryConnection(Twitter.class);
	    Twitter twitter = twitterCon.getApi();

	    LOG.debug("getScreenName : {}", twitter.userOperations().getUserProfile().getScreenName());

	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
	    for (Tweet t : tweets) {
		LOG.debug("getText : {}", t.getText());
	    }
	}

	return new ResponseEntity<>("OK!", HttpStatus.OK);
    }
}
