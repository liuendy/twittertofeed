package uk.co.landbit.twittertofeed.feed.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Item {

    // <title>Sometimes, being a cord cutter just isn&#8217;t worth it</title>
    // <link>http://thenextweb.com/opinion/2015/08/22/sometimes-being-a-cord-cutter-just-isnt-worth-it/</link>
    // <comments>http://thenextweb.com/opinion/2015/08/22/sometimes-being-a-cord-cutter-just-isnt-worth-it/#comments</comments>
    // <pubDate>Sat, 22 Aug 2015 17:30:24 +0000</pubDate>
    // <dc:creator><![CDATA[Nate Swanner]]></dc:creator>
    // <category><![CDATA[Opinion]]></category>
    //
    // <guid isPermaLink="false">http://thenextweb.com/?p=910535</guid>
    // <description><![CDATA[<img width="520" height="245"
    // src="http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/Internetblocked-520x245.jpg"
    // alt="Internetblocked" title="Sometimes, being a cord cutter just
    // isn&#039;t worth it" data-id="771318" /><br />‘Cord cutting’ is a phrase
    // tossed around pretty loosely. Over time, it’s come to represent a lot of
    // things, but being practical — it may just be a good bit of nonsense. The
    // overarching goal of cord cutting is to relieve yourself of a contract
    // with your cable company. The basic concept is to get TV without going
    // through popular avenues; consuming traditional media in an unconventional
    // way. That’s easy enough to do, but doesn’t always make sense for the end
    // user. The initial concern with cord cutting is bandwidth. To stream
    // media, you need a strong internet connection. According&#8230; <br><br><a
    // href="http://thenextweb.com/opinion/2015/08/22/sometimes-being-a-cord-cutter-just-isnt-worth-it/?utm_source=social&#038;utm_medium=feed&#038;utm_campaign=profeed">This
    // story continues</a> at The Next Web]]></description>
    // <wfw:commentRss>http://thenextweb.com/opinion/2015/08/22/sometimes-being-a-cord-cutter-just-isnt-worth-it/feed/</wfw:commentRss>
    // <slash:comments>0</slash:comments>
    @XmlElement
    private String title;
    @XmlElement
    private String link;
    @XmlElement
    private String uri;
    @XmlElement
    private String description;
    // <guid isPermaLink="false">http://thenextweb.com/?p=910535</guid>
    @XmlElement
    private String guid;
    @XmlElement
    private String pubDate;

    @XmlElement(name = "enclosure")
    private List<Enclosure> enclosures;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getLink() {
	return link;
    }

    public void setLink(String link) {
	this.link = link;
    }

    public String getUri() {
	return uri;
    }

    public void setUri(String uri) {
	this.uri = uri;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getGuid() {
	return guid;
    }

    public void setGuid(String guid) {
	this.guid = guid;
    }

    public String getPubDate() {
	return pubDate;
    }

    public void setPubDate(String pubDate) {
	this.pubDate = pubDate;
    }

    public List<Enclosure> getEnclosures() {
	return enclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
	this.enclosures = enclosures;
    }

}
