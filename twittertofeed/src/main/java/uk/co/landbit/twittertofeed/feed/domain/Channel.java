package uk.co.landbit.twittertofeed.feed.domain;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Channel {

    @XmlElement
    private String title;
    @XmlElement
    private String description;
    @XmlElement
    private String link;
    @XmlElement
    private String uri;
    @XmlElement
    private String copyright;
    @XmlElement
    private String pubDate;
    @XmlElement
    private String lastBuildDate;

    @XmlElement(name = "item")
    private List<Item> items;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    public List<Item> getItems() {
	return items;
    }

    public void setItems(List<Item> items) {
	this.items = items;
    }

    public String getCopyright() {
	return copyright;
    }

    public void setCopyright(String copyright) {
	this.copyright = copyright;
    }

    public String getPubDate() {
	return pubDate;
    }

    public void setPubDate(String pubDate) {
	this.pubDate = pubDate;
    }

    public String getLastBuildDate() {
	return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
	this.lastBuildDate = lastBuildDate;
    }

}
