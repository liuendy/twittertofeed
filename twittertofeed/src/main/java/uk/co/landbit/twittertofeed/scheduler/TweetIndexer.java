package uk.co.landbit.twittertofeed.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.co.landbit.twittertofeed.feed.service.FeedService;

@Component
public class TweetIndexer {

    private final static Logger LOG = LoggerFactory.getLogger(TweetIndexer.class);

    private final FeedService feedService;

    @Autowired
    public TweetIndexer(FeedService feedService) {
	super();
	this.feedService = feedService;
    }

    @Scheduled(fixedDelayString = "${twittertofeed.tweetindexer.scheduler}")
    public void indexTweets() {

	LOG.debug("Indexing tweets");
	
	feedService.indexTweets();
	
	LOG.debug("Indexing tweets done...");

    }
}
