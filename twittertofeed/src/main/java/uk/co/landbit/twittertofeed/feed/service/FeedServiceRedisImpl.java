package uk.co.landbit.twittertofeed.feed.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;
import uk.co.landbit.twittertofeed.feed.repository.FeedRedisRepository;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Service
public class FeedServiceRedisImpl implements FeedService {
    
    private static final Logger LOG = LoggerFactory.getLogger(FeedServiceRedisImpl.class);
    
    private final FeedRedisRepository feedRepository;
    private final UsersConnectionRepository usersConnectionRepository;
    private final UserRepository userRepository;
    
    @Inject
    public FeedServiceRedisImpl(FeedRedisRepository feedRepository, UserRepository userRepository, UsersConnectionRepository usersConnectionRepository) {
	super();
	this.feedRepository = feedRepository;
	this.userRepository = userRepository;
	this.usersConnectionRepository = usersConnectionRepository;
    }

    @Override
    public List<TweetEntry> indexTweets() {

	// get all twitter user
	List<User> users = userRepository.findBySignInProvider(SignInProvider.TWITTER);
	for (User u : users) {
	    ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(u.getEmail());
	    Connection<Twitter> twitterCon = connectionRepository.getPrimaryConnection(Twitter.class);
	    Twitter twitter = twitterCon.getApi();
	    LOG.debug("getScreenName : {}", twitter.userOperations().getUserProfile().getScreenName());

	    // get tweets
	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
	    	    
	    for (Tweet tweet : tweets) {
		// check if tweet is already indexed before parsing or index it
		TweetEntry te = feedRepository.getTweetEntry(String.valueOf(tweet.getId()));
		 
		if(te==null){
		    LOG.debug("Found new tweet, Indexing...");
		    te = new TweetEntry();
		    te.setId(String.valueOf(tweet.getId()));
		    te.setCreatedAt(String.valueOf(tweet.getCreatedAt()));
			    te.setText(tweet.getText());
		    te.setLink("http://landbit.co.uk");
		    feedRepository.saveTweetEntry(te);
		}
	    }
	}
	
	return null;
    }
    
    

}
