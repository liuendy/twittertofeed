package uk.co.landbit.twittertofeed.feed.service;

import java.util.List;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

public interface FeedService {

    List<TweetEntry> indexTweets();
    
    List<TweetEntry> getTweets(String uid);
    
    List<TweetEntry> getTweets(String uid, Integer page);
}
