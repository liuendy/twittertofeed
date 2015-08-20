package uk.co.landbit.twittertofeed.controller;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
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
import org.springframework.social.twitter.api.UrlEntity;
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
    public ResponseEntity<String> timeline(Model model) throws IOException {

	List<User> users = userRepository.findBySignInProvider(SignInProvider.TWITTER);
	for (User u : users) {
	    ConnectionRepository connectionRepository = usersConnectionRepository
		    .createConnectionRepository(u.getEmail());
	    Connection<Twitter> twitterCon = connectionRepository.getPrimaryConnection(Twitter.class);
	    Twitter twitter = twitterCon.getApi();

	    LOG.debug("getScreenName : {}", twitter.userOperations().getUserProfile().getScreenName());

	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
	    for (Tweet t : tweets) {
		if (t.getEntities().hasUrls()) {
		    List<UrlEntity> urls = t.getEntities().getUrls();
		    LOG.debug("Found {} urls in tweet", urls.size());

		    for (UrlEntity urlEntity : urls) {
			LOG.debug("Found url display {}, expanded {}", urlEntity.getDisplayUrl(), urlEntity.getExpandedUrl());
			
			//TODO service do execute instead of get to parse response
			Document doc = Jsoup.connect(urlEntity.getExpandedUrl())
				      .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
				      .referrer("http://landbit.co.uk")
				      .timeout(30000)
				      .followRedirects(true)
				      .ignoreContentType(true)
				      .get();
			String content = Jsoup.clean(doc.html(), Whitelist.basic());
			LOG.debug("Article content : {}", content );
		    }

		}
		LOG.debug("getText : {}", t.getText());
	    }
	}

	return new ResponseEntity<>("OK!", HttpStatus.OK);
    }
}
