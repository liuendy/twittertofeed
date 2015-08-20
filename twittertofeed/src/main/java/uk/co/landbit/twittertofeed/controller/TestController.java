package uk.co.landbit.twittertofeed.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.isolateAggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedOutput;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import uk.co.landbit.twittertofeed.user.domain.SignInProvider;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    
    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
    
    

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
    
    // TODO access feed by UUID to mitigate the fact that RSS is public
    @RequestMapping(value = "/api/rss", method = { RequestMethod.GET }, produces = { MediaType.TEXT_XML_VALUE +";charset=UTF-8"})
    public ResponseEntity<String> writeFeed(Model model) throws Exception {
	String rss=null;
	List<User> users = userRepository.findBySignInProvider(SignInProvider.TWITTER);
	for (User u : users) {
	    ConnectionRepository connectionRepository = usersConnectionRepository
		    .createConnectionRepository(u.getEmail());
	    Connection<Twitter> twitterCon = connectionRepository.getPrimaryConnection(Twitter.class);
	    Twitter twitter = twitterCon.getApi();

	    LOG.debug("getScreenName : {}", twitter.userOperations().getUserProfile().getScreenName());

	    List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline(3);
	    
	    
	    SyndFeed feed = new SyndFeedImpl();
//	    feed.setFeedType("rss_2.0");
//
//	    feed.setTitle("Sample Feed (created with ROME)");
//	    feed.setLink("http://rome.dev.java.net");
//	    feed.setDescription("This feed has been created using ROME (Java syndication utilities");
	    
	    feed.setFeedType("atom_1.0");
	    
            feed.setTitle("Atom Feed");
            feed.setLink("http://landbit.co.uk/");
            feed.setDescription("Your personalised feed");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String RFC3339DateString = sdf.format(new Date());
            Date udate = (Date) sdf.parseObject(RFC3339DateString);
 
            feed.setPublishedDate(udate);
	    
	    List<SyndEntry> entries = new ArrayList<>();
	    
	    
	    for (Tweet t : tweets) {
		if (t.getEntities().hasUrls()) {
		    List<UrlEntity> urls = t.getEntities().getUrls();
		    LOG.debug("Found {} urls in tweet", urls.size());

		    for (UrlEntity urlEntity : urls) {
			LOG.debug("Found url display {}, expanded {}", urlEntity.getDisplayUrl(), urlEntity.getExpandedUrl());
			
			
			HtmlFetcher fetcher = new HtmlFetcher();
			fetcher.setReferrer("http://landbit.co.uk");
			fetcher.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
			
			JResult res = fetcher.fetchAndExtract(urlEntity.getExpandedUrl(), 30000, true);
			String text = res.getText();
			String title = res.getTitle();
			String imageUrl = res.getImageUrl();
			
			

		      
		        SyndEntry entry;
		        SyndContent description;

		        entry = new SyndEntryImpl();
		        entry.setTitle(title);
		        entry.setLink(urlEntity.getExpandedUrl());
		        RFC3339DateString = sdf.format(new Date());
		        udate = (Date) sdf.parseObject(RFC3339DateString);
		 
		        entry.setPublishedDate(udate);
		        description = new SyndContentImpl();
		        description.setType("text/html");
		        description.setValue(text);
		        entry.setDescription(description);
		    
		        if(text!=null && !text.isEmpty()){
		        entries.add(entry);
		        }
		      
		        
			
			
			
			LOG.debug("Article content : {}", text );
		    }

		}
		LOG.debug("getText : {}", t.getText());
	    }
	    LOG.debug("RSS entries : {}", entries.size());
	    feed.setEntries(entries);

	    SyndFeedOutput output = new SyndFeedOutput();
	    rss = output.outputString(feed);
	    LOG.debug("RSS feed : {}", rss);
	}

	return new ResponseEntity<>(rss, HttpStatus.OK);
    }
    
   
}
