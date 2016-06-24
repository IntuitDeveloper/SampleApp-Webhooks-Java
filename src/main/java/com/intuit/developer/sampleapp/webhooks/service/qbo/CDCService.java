package com.intuit.developer.sampleapp.webhooks.service.qbo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.ipp.core.IEntity;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Logger;

/**
 * Class for implementing the QBO CDC api
 * 
 * @author dderose
 *
 */
@Service(value="CdcAPI")
public class CDCService implements QBODataService {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	@Autowired
    DataServiceFactory dataServiceFactory;

	@Override
	public void callDataService(CompanyConfig companyConfig) throws Exception {
		
		// create data service
		DataService service = dataServiceFactory.getDataService(companyConfig);
			
		try {
			LOG.info("Calling CDC ");
			// build entity list for cdc based on entities subscribed for webhooks
			List<String> subscribedEntities = Arrays.asList(companyConfig.getWebhooksSubscribedEntites().split(","));
			List<IEntity> entities = new ArrayList<>();
			for (String subscribedEntity : subscribedEntities) {
				Class<?> className = Class.forName("com.intuit.ipp.data." + subscribedEntity);
				IEntity entity = (IEntity) className.newInstance();
				entities.add(entity);
			}
			// call CDC
			service.executeCDCQuery(entities, companyConfig.getLastCdcTimestamp());	
			LOG.info("CDC complete ");
			
		} catch (Exception ex) {
			LOG.error("Error while calling CDC" , ex.getCause());
		}
		
	}

}
