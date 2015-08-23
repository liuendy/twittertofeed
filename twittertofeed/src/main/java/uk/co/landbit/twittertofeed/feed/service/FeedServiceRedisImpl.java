package uk.co.landbit.twittertofeed.feed.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.UrlEntity;
import org.springframework.stereotype.Service;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;
import uk.co.landbit.twittertofeed.feed.repository.TweetRedisRepository;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Service
public class FeedServiceRedisImpl implements FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedServiceRedisImpl.class);

    private final TweetRedisRepository feedRepository;
    private final UsersConnectionRepository usersConnectionRepository;
    private final UserRepository userRepository;
    private final DataMiningService dataMiningService;
    private final Map<String, String> fileExtensions;

    @Inject
    public FeedServiceRedisImpl(TweetRedisRepository feedRepository, UserRepository userRepository,
	    UsersConnectionRepository usersConnectionRepository, DataMiningService dataMiningService) {
	super();
	this.feedRepository = feedRepository;
	this.userRepository = userRepository;
	this.usersConnectionRepository = usersConnectionRepository;
	this.dataMiningService = dataMiningService;
	this.fileExtensions = new HashMap<>();
	this.fileExtensions.put("PNG", "image/png");
	this.fileExtensions.put("JPG", "image/jpeg");
	this.fileExtensions.put("JPEG", "image/jpeg");
	this.fileExtensions.put("GIF", "image/gif");
    }

    @Override
    public List<TweetEntry> indexTweets() {

	List<TweetEntry> tweetEntries = new ArrayList<>();

	// get all twitter user
	List<User> users = userRepository.findBySignInProvider(SignInProvider.TWITTER);

	for (User u : users) {
	    ConnectionRepository connectionRepository = usersConnectionRepository
		    .createConnectionRepository(u.getEmail());
	    Connection<Twitter> twitterCon = connectionRepository.getPrimaryConnection(Twitter.class);
	    Twitter twitter = twitterCon.getApi();
	    LOG.debug("Indexing tweets of user {}, getScreenName : {}", u.getId(), twitter.userOperations().getUserProfile().getScreenName());

	    // TODO be nice with twitter api and use sinceid (put sinceid in redis cache)
	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline(50);

	    // save a list of tweet ids belonging to an user
	    List<String> tweetids = new ArrayList<>();

	    if (tweets != null && tweets.size() > 0) {
		for (Tweet tweet : tweets) {
		    // extract urls
		    if (tweet.getEntities().hasUrls()) {

			// check if is already indexed
			TweetEntry te = feedRepository.getTweetEntry(String.valueOf(tweet.getId()));

			if (te == null) {
			    LOG.debug("Found new tweet, Indexing...");

			    // get only the first url for now TODO
			    LOG.debug("Found {} urls", tweet.getEntities().getUrls().size());
			    UrlEntity urlEntity = tweet.getEntities().getUrls().get(0);

			    te = new TweetEntry();
			    te.setId(String.valueOf(tweet.getId()));
			    te.setCreatedAt(tweet.getCreatedAt());
			    te.setTitle(tweet.getText());
			    te.setLink(urlEntity.getExpandedUrl());

			    // save first image if any
			    if (tweet.getEntities().hasMedia()) {
				te.setImgUrl(tweet.getEntities().getMedia().get(0).getMediaUrl());
			    }

			    // extract full article content
			    te = dataMiningService.fetchUrl(te);
			    
			    try {
				te.setMimeType(getContentType(te.getImgUrl()));
			    } catch (IOException e) {
				LOG.error("Can't extract MimeType for {}", te.getImgUrl());
			    }

			    // TODO get comments

			    tweetEntries.add(te);
			    tweetids.add(te.getId());
			    feedRepository.saveTweetEntry(te);
			}
		    }
		}
		feedRepository.addTweetIds(String.valueOf(u.getId()), tweetids);
	    }
	}

	return tweetEntries;
    }

    @Override
    public List<TweetEntry> getTweets(String uid) {
	List<TweetEntry> tweetEntries = new ArrayList<>();
	tweetEntries = feedRepository.findTweetEntriesByUid(uid);

	return tweetEntries;
    }

    @Override
    public List<TweetEntry> getTweets(String uid, Integer begin, Integer end) {
	List<TweetEntry> tweetEntries = new ArrayList<>();
	tweetEntries = feedRepository.findTweetEntriesByUid(uid, begin, end);

	return tweetEntries;
    }

    /**
     * TODO improve Dodgy mimetype extraction
     */
    public String getContentType(String fileName) throws IOException {
	LOG.debug("Extract mime type, {}", fileName);

	String extension = "";

	if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
	    int i = fileName.lastIndexOf('.');
	    if (i > 0) {
		extension = fileName.substring(i + 1);
	    }
	}
	return fileExtensions.get(extension.toUpperCase());
    }
}
