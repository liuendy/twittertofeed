package uk.co.landbit.twittertofeed.feed.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wuman.jreadability.Readability;

import uk.co.landbit.twittertofeed.feed.domain.TweetEntry;

@Service
public class DataMiningJReadabilityImpl implements DataMiningService {

    private final static Logger LOG = LoggerFactory.getLogger(DataMiningJReadabilityImpl.class);
    private final static Whitelist wl = Whitelist.relaxed();

    private final static String style = "<style type=\"text/css\">"+
	    "html * { font-size: 1em; line-height: 1.75em; color: #000; font-family: Georgia, serif !important;}"+
	    "h1,h2,h3,h4,h5,h6 { font-size: 1.25em; line-height: 1.25em; color: #76AABA;}"+
	    "p { margin: 2em 0; text-align: justify;}"+
	    "blockquote{ display:block; background: #fff; padding: 1em 1em 1em 1em; margin: 2em 2em 2em 2em; position: relative; color: #666; text-align: justify;"+
	    " border-left: 0.2em solid #76AABA; border-right: 0.1em solid #76AABA; -moz-box-shadow: 0.1em 0.1em 1em #ccc; -webkit-box-shadow: 0.1em 0.1em 1em #ccc;"+
	    " box-shadow: 0.1em 0.1em 1em #ccc;}"+
	    "blockquote::before{ content: \"\\201C\"; font-size: 2em; font-weight: bold; color: #999; position: absolute; left: 10px; top:5px;}"+
	    "blockquote::after{  content: \"\\201D\"; font-size: 2em; font-weight: bold; color: #999; position: absolute; right: 10px; bottom:5px;}"+
	    "blockquote a{ text-decoration: none; background: #eee; cursor: pointer; padding: 0 3px; color: #76AABA;}"+
	    "blockquote a:hover{ color: #666;}"+
	    "blockquote em{ font-style: italic;}"+
	    "</style>";

    private Readability extractor;

    public DataMiningJReadabilityImpl() {
	super();
	wl.addTags("figure");
    }

    @Override
    public TweetEntry fetchUrl(TweetEntry tweet) {

	try {
	    Connection.Response response = Jsoup.connect(tweet.getLink())
		    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
		    .referrer("http://landbit.co.uk").timeout(30000).followRedirects(true).ignoreContentType(true)
		    .validateTLSCertificates(false).execute();

	    Document doc = response.parse();

	    extractor = new Readability(doc);
	    extractor.init();
	    String content = extractor.html();

	    String clean = Jsoup.clean(content, wl);

	    Document mDocument = new Document("");
	    Element html = mDocument.createElement("html");
	    Element head = mDocument.createElement("head");
	    Element body = mDocument.createElement("body");

	    html.appendChild(head);
	    body = body.append(clean).select("div").first();
	    // ugly hack to put style in body, prevent display style in news summary
	    body.append(style);
	    html.appendChild(body);
	    mDocument.appendChild(html);

	    // TODO builders...
	    tweet.setText(mDocument.html());

	} catch (IllegalArgumentException iae) {
	    LOG.warn("Malformed URL [url={}, message={}]", tweet.getLink(), iae.getMessage());
	} catch (MalformedURLException mue) {
	    LOG.warn("Malformed URL [url={}, message={}]", new Object[] { tweet.getLink(), mue.getMessage() });
	} catch (HttpStatusException hse) {
	    LOG.warn("Cannot fetch site [url={}, statusMessage={}, statusCode={}]", tweet.getLink(), hse.getMessage(),
		    hse.getStatusCode());
	} catch (IOException ioe) {
	    LOG.warn("IOException. Cannot fetch site [url={}, errorMessage={}]", tweet.getLink(), ioe.getMessage());
	}

	return tweet;
    }
}
