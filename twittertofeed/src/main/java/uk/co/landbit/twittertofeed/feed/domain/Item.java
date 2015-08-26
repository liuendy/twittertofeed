package uk.co.landbit.twittertofeed.feed.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Item {

    @XmlElement
    private String title;

    @XmlElement
    private String author;

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

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

}
