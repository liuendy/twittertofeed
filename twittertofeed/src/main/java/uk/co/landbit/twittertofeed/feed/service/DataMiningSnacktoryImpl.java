package uk.co.landbit.twittertofeed.feed.service;

import org.springframework.stereotype.Service;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

@Service
public class DataMiningSnacktoryImpl implements DataMiningService {

    private HtmlFetcher fetcher;

    public DataMiningSnacktoryImpl() {
	super();
	this.fetcher = new HtmlFetcher();
	// TODO add random ref and agent to prevent eventual blacklist
	this.fetcher.setReferrer("http://yahoo.co.uk");
	this.fetcher.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
	this.fetcher.setLanguage("en-us");
    }

    @Override
    public TweetEntry fetchUrl(TweetEntry tweet) {
	JResult res;
	try {
	    res = fetcher.fetchAndExtract(tweet.getLink(), 40000, true);
	   
	    // TODO builders...
	    tweet.setText(res.getText());
	    if (tweet.getImgUrl() == null || tweet.getImgUrl().isEmpty()) {
		tweet.setImgUrl(res.getImageUrl());
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return tweet;
    }

}
