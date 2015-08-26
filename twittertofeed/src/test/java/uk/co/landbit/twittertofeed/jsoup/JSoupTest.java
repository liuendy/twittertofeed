package uk.co.landbit.twittertofeed.jsoup;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wuman.jreadability.Readability;

public class JSoupTest {
    private static final Logger LOG = LoggerFactory.getLogger(JSoupTest.class);

//    @Test
//    public void shouldCleanHTMLContent() throws IOException {
////	// http://trib.al/KsvH2JE
////	// http://tcrn.ch/1NxNAZJ
////	String url = "http://trib.al/KsvH2JE";
////	Document doc = Jsoup.connect(url)
////		.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0")
////		.referrer("http://landbit.co.uk").timeout(30000).followRedirects(true).ignoreContentType(true).get();
////
////	doc.outputSettings(new Document.OutputSettings().prettyPrint(true));
////	//
////	// doc.select("br").append("\\n");
////	// doc.select("p").prepend("\\n\\n");
////	// String s = doc.html().replaceAll("\\\\n", "\n");
////	Whitelist clean = Whitelist.simpleText().addTags("blockquote", "cite", "code", "p", "q", "s", "strike");
////
////	// String content = Jsoup.clean(doc.html(), Whitelist.none());
////
////	// TODO check with baseuri
////	String content = Jsoup.clean(doc.html(), "", clean, new Document.OutputSettings().prettyPrint(true));
////	// String content = "RT amazing news https://bla with links
////	// http://blabla.com http://trib.al/KsvH2JE";
////	// content = content.replaceAll("https?://\\S+\\s?", "");
////	// content = content.replaceAll("https?://\\S+\\s?", "");
////	// String content = Jsoup.clean(content, Whitelist.none());
////	LOG.debug("Article content : {}", content);
//    }
//
//    @Test
//    public void shouldConvertHTMLtoReadabilityLikeContent() throws Exception {
//
////	// http://trib.al/KsvH2JE
////	// http://tcrn.ch/1NxNAZJ
////	String url = "http://trib.al/KsvH2JE";
////
////	HtmlFetcher fetcher = new HtmlFetcher();
////	// set cache. e.g. take the map implementation from google collections:
////	// fetcher.setCache(new
////	// MapMaker().concurrencyLevel(20).maximumSize(count).
////	// expireAfterWrite(minutes, TimeUnit.MINUTES).makeMap();
////	fetcher.setReferrer("http://landbit.co.uk");
////	fetcher.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
////
////	JResult res = fetcher.fetchAndExtract(url, 30000, true);
////	String text = res.getText();
////	String title = res.getTitle();
////	String imageUrl = res.getImageUrl();
////	LOG.debug("Article content : {}", title);
////	LOG.debug("Article content : {}", imageUrl);
////	LOG.debug("Article content : {}", text);
//
//    }
//
//    @Test
//    public void shouldCleanHTMLContentBoilerPipe() throws IOException, BoilerpipeProcessingException, SAXException {
////	// http://trib.al/KsvH2JE
////	// http://tcrn.ch/1NxNAZJ
////	String urll = "http://trib.al/KsvH2JE";
////	URL url = new URL(urll);
////	// String text = ArticleExtractor.INSTANCE.getText(url);
////
////	final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
////
////	// choose the operation mode (i.e., highlighting or extraction)
////	// final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();
////	final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();
////
////	LOG.debug("Article content : {}", hh.process(url, extractor));
//    }

    @Test
    public void shouldCleanHTMLContentReadability() throws IOException  {
	// http://trib.al/KsvH2JE
	// http://tcrn.ch/1NxNAZJ
	String urll = "http://tcrn.ch/1NxNAZJ";
	URL url = new URL(urll);

	
	final Readability extractor = new Readability(url, 10000);
	extractor.init();
	String content = extractor.html();

	Whitelist wl = Whitelist.relaxed();
	// add additional tags here as necessary
	wl.addTags("figure"); 
	String clean = Jsoup.clean(content, wl);
	
	Document mDocument = new Document("");
	Element html = mDocument.createElement("html");
	Element head = mDocument.createElement("head");
	Element body = mDocument.createElement("body");
	
	
	html.appendChild(head);
	body = body.append(clean).select("div").first();
	html.appendChild(body);
	mDocument.appendChild(html);
	
//	Document document = Jsoup.parse(content);
//	Element head = document.head();
//	String style = "<STYLE type=\"text/css\">"+
//		"blockquote{"+
//		"  display:block;"+
//		"  background: #fff;"+
//		"  padding: 15px 20px 15px 45px;"+
//		"  margin: 0 0 20px;"+
//		"  position: relative;"+
//		"  "+
//		"  /*Font*/"+
//		"  font-family: Georgia, serif;"+
//		"  font-size: 16px;"+
//		"  line-height: 1.2;"+
//		"  color: #666;"+
//		"  text-align: justify;"+
//		"  "+
//		"  /*Borders - (Optional)*/"+
//		"  border-left: 15px solid #76AABA;"+
//		"  border-right: 2px solid #76AABA;"+
//		"  "+
//		"  /*Box Shadow - (Optional)*/"+
//		"  -moz-box-shadow: 2px 2px 15px #ccc;"+
//		"  -webkit-box-shadow: 2px 2px 15px #ccc;"+
//		"  box-shadow: 2px 2px 15px #ccc;"+
//		"}"+
//		""+
//		"blockquote::before{"+
//		"  content: \"\\201C\"; /*Unicode for Left Double Quote*/"+
//		"  "+
//		"  /*Font*/"+
//		"  font-family: Georgia, serif;"+
//		"  font-size: 60px;"+
//		"  font-weight: bold;"+
//		"  color: #999;"+
//		"  "+
//		"  /*Positioning*/"+
//		"  position: absolute;"+
//		"  left: 10px;"+
//		"  top:5px;"+
//		"}"+
//		""+
//		"blockquote::after{"+
//		"  /*Reset to make sure"+
//		"  content: \"\";*/"+
//		"  "+
//		"   content: \"\\201D\"; /*Unicode for Left Double Quote*/"+
//		"  "+
//		"  /*Font*/"+
//		"  font-family: Georgia, serif;"+
//		"  font-size: 60px;"+
//		"  font-weight: bold;"+
//		"  color: #999;"+
//		"  "+
//		"  /*Positioning*/"+
//		"  position: absolute;"+
//		"  right: 10px;"+
//		"  bottom:5px;"+
//		"}"+
//		""+
//		"blockquote a{"+
//		"  text-decoration: none;"+
//		"  background: #eee;"+
//		"  cursor: pointer;"+
//		"  padding: 0 3px;"+
//		"  color: #76AABA;"+
//		"}"+
//		""+
//		"blockquote a:hover{"+
//		" color: #666;"+
//		"}"+
//		""+
//		"blockquote em{"+
//		"  font-style: italic;"+
//		"}"+
//		"</STYLE>";
//	head.append(style);	
//	
//	content = document.html();
	LOG.debug("Article content : {}", mDocument.html());

	//LOG.debug("Article content outer: {}", contentOuter);

    }

}
