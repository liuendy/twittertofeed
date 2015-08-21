package uk.co.landbit.twittertofeed.feed.repository;

import javax.inject.Inject;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Component;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

@Component
public class FeedRedisRepository {

    private final StringRedisTemplate redisTemplate;
      
    @Inject
    public FeedRedisRepository(StringRedisTemplate redisTemplate) {
	
	this.redisTemplate = redisTemplate;
//	valueOps = template.opsForValue();
//	users = new DefaultRedisList<String>(KeyUtils.users(), template);
//	timeline = new DefaultRedisList<String>(KeyUtils.timeline(), template);
//	userIdCounter = new RedisAtomicLong(KeyUtils.globalUid(), template.getConnectionFactory());
//	postIdCounter = new RedisAtomicLong(KeyUtils.globalPid(), template.getConnectionFactory());
    }
    
    public void saveTweetEntry(TweetEntry tweet){
	//HashOperations<String, String, String> hashOperations = template.opsForHash();
	redisTemplate.opsForHash().put("tweetentries", tweet.getId(), tweet);
    }
    
    public TweetEntry getTweetEntry(String key){
	TweetEntry tweet = null;
	
	tweet = (TweetEntry) redisTemplate.opsForHash().get("tweetentries", key);
	
	return tweet;
    }
    
    // collections mapping the core data structures

//    private RedisMap<String, String> feed(String fid) {
//	return new DefaultRedisMap<String, String>("fid:" + fid, template);
//    }
//
//    private RedisList<String> feeds(String uid) {
//	return new DefaultRedisList<String>("uid:" + uid + ":feeds", template);
//    }

    // various util methods

}
