package com.intuit.developer.sampleapp.webhooks.service.qbo;

import org.springframework.stereotype.Service;

import com.intuit.ipp.services.WebhooksService;

/**
 * 
 * @author dderose
 *
 */
@Service
public class WebhooksServiceFactory {
	
	/**
	 * Initializes WebhooksService 
	 * 
	 * @return
	 */
	public WebhooksService getWebhooksService() {
		
		//create WebhooksService
		return new WebhooksService();
	}

}
