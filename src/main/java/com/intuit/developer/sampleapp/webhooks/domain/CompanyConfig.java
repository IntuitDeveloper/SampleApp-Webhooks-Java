package com.intuit.developer.sampleapp.webhooks.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity to store oauth tokens and other configs for the QBO company
 * 
 * @author dderose
 *
 */
@Entity
public class CompanyConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String realmId; //companyId
	
	private String accessToken;
	
	private String accessTokenSecret;
	
	private String webhooksSubscribedEntites; // this will be a comma separated list of entities
	
	private String lastCdcTimestamp; // timestamp when the last CDC call was made
	
	public CompanyConfig(String realmId, String accessToken, String accessTokenSecret, String webhooksSubscribedEntites) {
        this.realmId = realmId;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.webhooksSubscribedEntites = webhooksSubscribedEntites;
    }

	public CompanyConfig() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRealmId() {
		return realmId;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	public String getWebhooksSubscribedEntites() {
		return webhooksSubscribedEntites;
	}

	public void setWebhooksSubscribedEntites(String webhooksSubscribedEntites) {
		this.webhooksSubscribedEntites = webhooksSubscribedEntites;
	}

	public String getLastCdcTimestamp() {
		return lastCdcTimestamp;
	}

	public void setLastCdcTimestamp(String lastCdcTimestamp) {
		this.lastCdcTimestamp = lastCdcTimestamp;
	}

	
}
