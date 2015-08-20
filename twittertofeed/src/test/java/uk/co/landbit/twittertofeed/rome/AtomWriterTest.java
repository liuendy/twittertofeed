package uk.co.landbit.twittertofeed.rome;

import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedOutput;

public class AtomWriterTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(AtomWriterTest.class);
    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");
    
    @Test
    public void shouldConvertHTMLtoReadabilityLikeContent() throws Exception {

	
	SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");

        feed.setTitle("Sample Feed (created with ROME)");
        feed.setLink("http://rome.dev.java.net");
        feed.setDescription("This feed has been created using ROME (Java syndication utilities");
        feed.setPublishedDate(new Date());

        List<SyndEntry> entries = new ArrayList<>();
        SyndEntry entry;
        SyndContent description;

        entry = new SyndEntryImpl();
        entry.setTitle("ROME v1.0");
        entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome01");
        entry.setPublishedDate(DATE_PARSER.parse("2004-06-08"));
        description = new SyndContentImpl();
        description.setType("text/plain");
        description.setValue("Initial release of ROME");
        entry.setDescription(description);
        entries.add(entry);


        entry = new SyndEntryImpl();
        entry.setTitle("ROME v3.0");
        entry.setLink("http://wiki.java.net/bin/view/Javawsxml/Rome03");
        entry.setPublishedDate(DATE_PARSER.parse("2004-07-27"));
        description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue("<p>More Bug fixes, mor API changes, some new features and some Unit testing</p>"+
                             "<p>For details check the <a href=\"https://rometools.jira.com/wiki/display/ROME/Change+Log#ChangeLog-Changesmadefromv0.3tov0.4\">Changes Log</a></p>");
        entry.setDescription(description);
        entries.add(entry);

        feed.setEntries(entries);

       // Writer writer = new FileWriter("feed.rss");
        SyndFeedOutput output = new SyndFeedOutput();
        String rss = output.outputString(feed);
        
        LOG.debug("RSS feed : {}", rss);
        
        //output.output(feed,writer);
        //writer.close();

	
    }
    
}
