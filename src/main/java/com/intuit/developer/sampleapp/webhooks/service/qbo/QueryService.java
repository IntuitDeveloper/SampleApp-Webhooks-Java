package com.intuit.developer.sampleapp.webhooks.service.qbo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Logger;

/**
 * Class for implementing QBO Query api 
 * 
 * @author dderose
 *
 */
@Service(value="QueryAPI")
public class QueryService implements QBODataService {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	@Autowired
    DataServiceFactory dataServiceFactory;

	@Override
	public void callDataService(CompanyConfig companyConfig) throws Exception {
		
		// create data service
		DataService service = dataServiceFactory.getDataService(companyConfig);
			
		try {
			LOG.info("Calling Query API ");
			String query = "select * from ";
			//Build query list for each subscribed entities
			List<String> subscribedEntities = Arrays.asList(companyConfig.getWebhooksSubscribedEntites().split(","));
			subscribedEntities.forEach(entity -> executeQuery(query + entity, service)) ;
			
		} catch (Exception ex) {
			LOG.error("Error loading app configs" , ex.getCause());
		}
		
	}
	
	/**
	 * Call executeQuery api for each entity
	 * 
	 * @param query
	 * @param service
	 */
	public void executeQuery(String query, DataService service) {
		try {
			LOG.info("Executing Query " + query);
			service.executeQuery(query);
			LOG.info(" Query complete" );
		} catch (Exception ex) {
			LOG.error("Error loading app configs" , ex.getCause());
		}
	}

}
