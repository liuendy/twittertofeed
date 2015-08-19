package uk.co.landbit.twittertofeed.social.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * Represent the data associated with a social account.
 * This class exists just to iterate on all elements
 * 
 * See class UsersConnectionRepository
 */
@Entity
@Table(name = "UserConnection")
public class UserConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserConnectionPK pk;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "profileUrl")
    private String profileUrl;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "accessToken")
    private String accessToken;

    @Column(name = "secret")
    private String secret;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "expireTime")
    private Long expireTime;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getProfileUrl() {
	return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
	this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
	return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    public String getAccessToken() {
	return accessToken;
    }

    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    public String getSecret() {
	return secret;
    }

    public void setSecret(String secret) {
	this.secret = secret;
    }

    public String getRefreshToken() {
	return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
	this.refreshToken = refreshToken;
    }

    public Long getExpireTime() {
	return expireTime;
    }

    public void setExpireTime(Long expireTime) {
	this.expireTime = expireTime;
    }

    public UserConnectionPK getPk() {
	return pk;
    }

    public void setPk(UserConnectionPK pk) {
	this.pk = pk;
    }

}
