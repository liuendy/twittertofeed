package uk.co.landbit.twittertofeed.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

public class JSoupTest {
    private static final Logger LOG = LoggerFactory.getLogger(JSoupTest.class);
    
//    @Test
//    public void shouldCleanHTMLContent() throws IOException  {
//	// http://trib.al/KsvH2JE
//	// http://tcrn.ch/1NxNAZJ
//	String url = "http://trib.al/KsvH2JE";
//	Document doc = Jsoup.connect(url)
//		.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
//		.referrer("http://landbit.co.uk").timeout(30000).followRedirects(true).ignoreContentType(true).get();
//
//		
////	doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
////	
////	doc.select("br").append("\\n");
////	doc.select("p").prepend("\\n\\n");
////	String s = doc.html().replaceAll("\\\\n", "\n");
//
//	//String content = Jsoup.clean(doc.html(), Whitelist.none());
//
//	//String content = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
//
//	String content = Jsoup.clean(doc.html(), "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
//	LOG.debug("Article content : {}", content);
//    }
    
    @Test
    public void shouldConvertHTMLtoReadabilityLikeContent() throws Exception {

	// http://trib.al/KsvH2JE
	// http://tcrn.ch/1NxNAZJ
	String url = "http://trib.al/KsvH2JE";

	HtmlFetcher fetcher = new HtmlFetcher();
	// set cache. e.g. take the map implementation from google collections:
	// fetcher.setCache(new
	// MapMaker().concurrencyLevel(20).maximumSize(count).
	// expireAfterWrite(minutes, TimeUnit.MINUTES).makeMap();
	fetcher.setReferrer("http://landbit.co.uk");
	fetcher.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
	
	
	JResult res = fetcher.fetchAndExtract(url, 30000, true);
	String text = res.getText();
	String title = res.getTitle();
	String imageUrl = res.getImageUrl();
	LOG.debug("Article content : {}", title);
	LOG.debug("Article content : {}", imageUrl);
	LOG.debug("Article content : {}", text);
	
    }
}
