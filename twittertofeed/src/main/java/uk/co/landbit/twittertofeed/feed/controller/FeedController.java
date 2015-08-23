package uk.co.landbit.twittertofeed.feed.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.co.landbit.twittertofeed.feed.domain.Channel;
import uk.co.landbit.twittertofeed.feed.domain.Enclosure;
import uk.co.landbit.twittertofeed.feed.domain.Item;
import uk.co.landbit.twittertofeed.feed.domain.RSSFeed;
import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;
import uk.co.landbit.twittertofeed.feed.service.FeedService;

// TODO optimization : Stream and Non blocking endpoint
/**
 * Main controller, API
 * 
 * This generate your Rss Feed 
 *
 */
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);
    private static final String UTF8 = ";charset=UTF-8";
    private static final Integer RANGE_BEGIN = 0;
    
    private final FeedService feedService;
  
    @Autowired
    public FeedController(FeedService feedService) {
	this.feedService = feedService;
    }
    
    // TODO better exception handling, code refactoring
    @RequestMapping(value = "/{uuid}/rss",
            method = {RequestMethod.GET},
            produces = {  MediaType.APPLICATION_XML_VALUE + UTF8 })
    public ResponseEntity<RSSFeed> writeRssFeed(@PathVariable final String uuid,
	    @RequestParam(value="nb", required=false, defaultValue="299") final Integer nb) {

	RSSFeed rssFeed = new RSSFeed();

	// TODO review dates...
	SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z", Locale.US);

	Channel rssChannel = new Channel();
	rssChannel.setTitle("Twitter to Feed");
	rssChannel.setDescription("Your personalised twitter timeline as a syndication feed");
	rssChannel.setLink("http://landbit.co.uk/");
	rssChannel.setLastBuildDate(sdf.format(new Date()));

	// get cached tweets
	List<TweetEntry> tweets = feedService.getTweets(uuid, RANGE_BEGIN, nb-1);

	List<Item> items = new ArrayList<>();

	for (TweetEntry t : tweets) {

	    Item i = new Item();
	    i.setTitle(t.getTitle());
	    i.setLink(t.getLink());
	    i.setGuid(t.getLink());
	    i.setPubDate(sdf.format(t.getCreatedAt()));
	    i.setDescription(t.getText());

	    // images
	    List<Enclosure> images = new ArrayList<>();
	    Enclosure image = new Enclosure();
	    image.setUrl(t.getImgUrl());
	    image.setType(t.getMimeType());
	    image.setLength(0);
	    images.add(image);
	    if (image.getType() != null) {
		i.setEnclosures(images);
	    }

	    if (t.getText() != null && !t.getText().isEmpty()) {
		items.add(i);
	    }
	}

	LOG.debug("RSS entries : {}", items.size());

	rssChannel.setItems(items);
	rssFeed.setChannel(rssChannel);

	return new ResponseEntity<>(rssFeed, HttpStatus.OK);
    }
}
