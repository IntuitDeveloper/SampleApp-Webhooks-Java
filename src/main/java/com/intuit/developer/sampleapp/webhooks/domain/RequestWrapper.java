package com.intuit.developer.sampleapp.webhooks.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Wrapper class for the webhooks payload
 * @author dderose
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestWrapper {

	List<EventNotification> eventNotifications;

	public List<EventNotification> getEventNotifications() {
		return eventNotifications;
	}

	public void setEventNotifications(List<EventNotification> eventNotifications) {
		this.eventNotifications = eventNotifications;
	}
	
}
