package com.intuit.developer.sampleapp.webhooks.service.qbo;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;

/**
 * Interface holding methods to call QBP Dataservice api
 * 
 * @author dderose
 *
 */
public interface QBODataService {
	
	public void callDataService(CompanyConfig companyConfig) throws Exception;

}
