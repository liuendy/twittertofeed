package uk.co.landbit.twittertofeed.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.landbit.twittertofeed.social.domain.UserConnection;
import uk.co.landbit.twittertofeed.social.repository.UserConnectionRepository;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    private Twitter twitter;

    private ConnectionRepository connectionRepository;
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private UserConnectionRepository userConnectionRepository;

    private @Value("${spring.social.twitter.app-id}") String appId;
    private @Value("${spring.social.twitter.app-secret}") String appSecret;

    @Autowired
    public TestController(Twitter twitter, ConnectionRepository connectionRepository,
	    UsersConnectionRepository usersConnectionRepository) {
	this.twitter = twitter;
	this.connectionRepository = connectionRepository;
	this.usersConnectionRepository = usersConnectionRepository;
    }

    @RequestMapping(value = "/api/timeline", method = { RequestMethod.GET }, produces = { "application/json" })
    public ResponseEntity<String> timeline(Model model) {

	// connectionRepository.findUserIdsConnectedTo
	// usersConnectionRepository.findUserIdsConnectedTo("twitter", null);

	// if (connectionRepository.findPrimaryConnection(Twitter.class) ==
	// null) {
	// return "redirect:/connect/twitter";
	// }

	// Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret,
	// accessToken, accessTokenSecret);

	// connectionRepository.findAllConnections()

	// Set<String> ids =
	// usersConnectionRepository.findUserIdsConnectedTo("twitter", null);

	List<UserConnection> userConnections = userConnectionRepository.findByPkProviderId("twitter");
	//List<UserConnection> userConnections = userConnectionRepository.findAll();

	LOG.debug("Number of Ids retrieved : {}", userConnections.size());

	for (UserConnection userConnection : userConnections) {

	    // ConnectionRepository connectionRepository =
	    // usersConnectionRepository.createConnectionRepository(id);
	    // Connection<Twitter> twitterCon =
	    // connectionRepository.getPrimaryConnection(Twitter.class);
	    // Twitter twitter = twitterCon.getApi();

	    Twitter twitter = new TwitterTemplate(appId, appSecret, userConnection.getAccessToken(),
		    userConnection.getSecret());
	    LOG.debug("getScreenName : {}", twitter.userOperations().getUserProfile().getScreenName());

	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
	    for (Tweet t : tweets) {
		LOG.debug("getText : {}", t.getText());
	    }
	}

	// List<Connection> connections =
	// connectionRepository.findAllConnections();
	// for (Connection conn : connections) {
	// Twitter twitter = new TwitterTemplate(twitterAppKey,
	// twitterAppSecret, conn.getAccessToken(), conn.getSecret());
	// //now I can run the operations
	// System.out.println(twitter.userOperations().getScreenName());
	// }

	return new ResponseEntity<>("OK!", HttpStatus.OK);
    }

}
