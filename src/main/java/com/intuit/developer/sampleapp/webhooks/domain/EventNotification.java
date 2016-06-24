package com.intuit.developer.sampleapp.webhooks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author dderose
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventNotification {

	String realmId;
	Event dataChangeEvent;
	
	public String getRealmId() {
		return realmId;
	}
	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}
	public Event getDataChangeEvent() {
		return dataChangeEvent;
	}
	public void setDataChangeEvent(Event dataChangeEvent) {
		this.dataChangeEvent = dataChangeEvent;
	}
	
	
}
