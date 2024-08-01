package com.intuit.developer.sampleapp.webhooks.service.queue;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.service.CompanyConfigService;
import com.intuit.developer.sampleapp.webhooks.service.qbo.QBODataService;
import com.intuit.developer.sampleapp.webhooks.service.qbo.WebhooksServiceFactory;
import com.intuit.ipp.data.EventNotification;
import com.intuit.ipp.data.WebhooksEvent;
import com.intuit.ipp.services.WebhooksService;
import com.intuit.ipp.util.DateUtils;
import com.intuit.ipp.util.Logger;


/**
 * Callable task to process the queue
 * 1. Retrieves the payload from the queue
 * 2. Converts json to object
 * 3. Queries CompanyConfig table to get the last CDC performed time for the realmId
 * 4. Performs CDC for all the subscribed entities using the lastCDCTime retrieved in step 3
 * 5. Updates the CompanyConfig table with the last CDC performed time for the realmId - time when step 4 was performed
 * 
 * @author dderose
 *
 */
@Service
public class QueueProcessor implements Callable<Object> {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	@Autowired
	@Qualifier("CdcAPI")
	private QBODataService cdcService;
	
	@Autowired
	private QueueService queueService;
	
	@Autowired
	private CompanyConfigService companyConfigService;
	
	@Autowired
    WebhooksServiceFactory webhooksServiceFactory;
	
	public static final String DATE_yyyyMMddTHHmmssSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	@Override
	public Object call() throws Exception { 
		
		while (!queueService.getQueue().isEmpty()) {
			//remove item from queue
			String payload = queueService.getQueue().poll();
			LOG.info("processing payload: Queue Size:" + queueService.getQueue().size());
			
			// create webhooks service
			WebhooksService service = webhooksServiceFactory.getWebhooksService();
			
			//Convert payload to obj
			WebhooksEvent event = service.getWebhooksEvent(payload);
		  for (EventNotification eventNotification : event.getEventNotifications()) {

        // get the company config
        CompanyConfig companyConfig = companyConfigService.getCompanyConfigByRealmId(eventNotification.getRealmId());

        // perform cdc with last updated timestamp and subscribed entities
      	String cdcTimestamp = DateUtils.getStringFromDateTime(DateUtils.getCurrentDateTime());
        cdcService.callDataService(companyConfig);

        // update cdcTimestamp in companyconfig
      	companyConfig.setLastCdcTimestamp(cdcTimestamp);
        companyConfigService.save(companyConfig);
      }
		}
		
		return null;
	}


}
