package com.intuit.developer.sampleapp.webhooks.domain;

/**
 * @author dderose
 *
 */
public class ResponseWrapper {
	
	private String message;
	
	public ResponseWrapper(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
