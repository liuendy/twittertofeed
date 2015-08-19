package uk.co.landbit.twittertofeed.social.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserConnectionPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "userId")
    private String userId;

    @Column(name = "providerId")
    private String providerId;

    @Column(name = "providerUserId")
    private String providerUserId;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getProviderId() {
	return providerId;
    }

    public void setProviderId(String providerId) {
	this.providerId = providerId;
    }

    public String getProviderUserId() {
	return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
	this.providerUserId = providerUserId;
    }

}
