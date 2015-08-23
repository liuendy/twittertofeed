package uk.co.landbit.twittertofeed.feed.service;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

public interface DataMiningService {
    
    TweetEntry fetchUrl(TweetEntry tweet);

}
