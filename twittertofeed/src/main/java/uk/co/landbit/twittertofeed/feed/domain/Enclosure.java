package uk.co.landbit.twittertofeed.feed.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Enclosure {

    @XmlAttribute
    private String url;
   
    @XmlAttribute
    private String type;
    
    @XmlAttribute
    private long length;

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public long getLength() {
	return length;
    }

    public void setLength(long length) {
	this.length = length;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

}
