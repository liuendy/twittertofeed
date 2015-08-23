package uk.co.landbit.twittertofeed.feed.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Component;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

@Component
public class TweetRedisRepository {

    private final StringRedisTemplate redisTemplate;

    @Inject
    public TweetRedisRepository(StringRedisTemplate redisTemplate) {

	this.redisTemplate = redisTemplate;
	// valueOps = template.opsForValue();
	// users = new DefaultRedisList<String>(KeyUtils.users(), template);
	// timeline = new DefaultRedisList<String>(KeyUtils.timeline(),
	// template);
	// userIdCounter = new RedisAtomicLong(KeyUtils.globalUid(),
	// template.getConnectionFactory());
	// postIdCounter = new RedisAtomicLong(KeyUtils.globalPid(),
	// template.getConnectionFactory());
    }

    public void saveTweetEntry(TweetEntry tweet) {
	// HashOperations<String, String, String> hashOperations =
	// template.opsForHash();
	//redisTemplate.opsForHash().put("tweetentries", tweet.getId(), tweet);
	tweetEntries().put(tweet.getId(), tweet);
    }
    
    public void addTweetIds(String uid, List<String> tweetids){
	redisTemplate.opsForList().leftPushAll("uid:" + uid + ":tweetids", tweetids);
	redisTemplate.opsForList().trim("uid:" + uid + ":tweetids", 0, 299);
	//uidTweets(uid).addAll(tweetids);
    }

    
    public TweetEntry getTweetEntry(String key) {
	TweetEntry tweet = null;

	//tweet = (TweetEntry) redisTemplate.opsForHash().get("tweetentries", key);
	tweet= tweetEntries().get(key);
	return tweet;
    }

    public List<TweetEntry> findTweetEntriesByUid(String uid) {

	List<String> tweetIds = findTweetIdsByUid(uid, 0, -1);
	List<TweetEntry> tweets = new ArrayList<>();
	
	for(String id : tweetIds){
	    TweetEntry te = tweetEntries().get(id);
	    tweets.add(te);
	}
	return tweets;
    }
    
    public List<TweetEntry> findTweetEntriesByUid(String uid, Integer begin, Integer end) {

   	List<String> tweetIds = findTweetIdsByUid(uid, begin, end);
   	List<TweetEntry> tweets = new ArrayList<>();
   	
   	for(String id : tweetIds){
   	    TweetEntry te = tweetEntries().get(id);
   	    tweets.add(te);
   	}
   	return tweets;
       }

    // TODO return a subset, list capping
    public List<String> findTweetIdsByUid(String uid, Integer begin, Integer end) {

	List<String> tweetIds = new ArrayList<String>();
	tweetIds = uidTweets(uid).range(begin, end);
	return tweetIds;
    }

    // collections mapping the core data structures

//    private RedisMap<String, String> feed(String fid) {
//	return new DefaultRedisMap<String, String>("fid:" + fid, redisTemplate);
//    }

    private RedisMap<String, TweetEntry> tweetEntries() {
	return new DefaultRedisMap<String, TweetEntry>("tweetentries", redisTemplate);
    }

    private RedisList<String> uidTweets(String uid) {
	return new DefaultRedisList<String>("uid:" + uid + ":tweetids", redisTemplate);
    }

    // various util methods

}
