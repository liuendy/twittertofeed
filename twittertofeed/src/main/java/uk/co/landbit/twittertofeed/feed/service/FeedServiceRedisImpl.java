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

    private final static Logger LOG = LoggerFactory.getLogger(FeedServiceRedisImpl.class);
    private final static Integer CHUNK = 30;
    private final static Integer MAX = 300;

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
	    Long uid = u.getId();
	    LOG.debug("Indexing tweets of user {}, getScreenName : {}", uid, twitter.userOperations().getUserProfile().getScreenName());

	    // TODO be nice with twitter api and use sinceid (put sinceid in redis cache)
	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline(50);
	    LOG.debug("Received {} from twitter API", tweets.size());

	    // get index of where we stopped during previous tweet index
	    Integer page = 0;
	    Integer lastIndex = feedRepository.getLastIndex(uid);
	    LOG.debug("LastIndex {}, User {}", lastIndex, uid);
	    
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
			    te.setTitle(tweet.getUser().getScreenName() + " - " + tweet.getText().replaceAll("https?://\\S+\\s?", ""));
			    te.setLink(urlEntity.getExpandedUrl());
			    te.setAuthor(tweet.getUser().getName());
			   
			    // save first image if any
			    if (tweet.getEntities().hasMedia()) {
				te.setImgUrl(tweet.getEntities().getMedia().get(0).getMediaUrl());
			    }else{
				te.setImgUrl(tweet.getUser().getProfileImageUrl());
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
			    
			    lastIndex=incLastIndex(lastIndex);
			    page = getPage(lastIndex);
			    feedRepository.saveTweetEntry(te);
			    feedRepository.addTweetId( uid, te.getId(),  page,  CHUNK);
			    feedRepository.setLastIndex(uid, lastIndex);
			} else {
			    // already indexed, just add tweet id to user's list if not present already
			    boolean isIdAlreadyExistsForUser = feedRepository.isIdExistsForUser(te.getId(), uid);
			    if (!isIdAlreadyExistsForUser) {
				lastIndex = incLastIndex(lastIndex);
				page = getPage(lastIndex);
				feedRepository.addTweetId(uid, te.getId(), page, CHUNK);
				feedRepository.setLastIndex(uid, lastIndex);
			    }
			}
		    }
		}
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
    public List<TweetEntry> getTweets(String uid, Integer page) {
	List<TweetEntry> tweetEntries = new ArrayList<>();
	tweetEntries = feedRepository.findTweetEntriesByUidAndPage(uid, page, CHUNK);

	return tweetEntries;
    }

    /**
     * TODO improve Dodgy mimetype extraction
     */
    private String getContentType(String fileName) throws IOException {
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
    
    private Integer incLastIndex(Integer lastIndex) {
	lastIndex = lastIndex + 1;
	if (lastIndex > MAX) {
	    lastIndex = 0;
	}
	return lastIndex;
    }

    /**
     * Calculate the page for a new item we're doing some sort of sharding here
     */
    private Integer getPage(Integer lastIndex) {

	return lastIndex / CHUNK;
    }
}
