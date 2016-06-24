package com.intuit.developer.sampleapp.webhooks.service.qbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.developer.sampleapp.webhooks.domain.AppConfig;
import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuthAuthorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Config;

/**
 * 
 * @author dderose
 *
 */
@Service
public class DataServiceFactory {
	
	@Autowired
    AppConfig appConfig;
	
	/**
	 * Initializes DataService for a given app/company profile
	 * 
	 * @param companyConfig
	 * @return
	 * @throws FMSException
	 */
	public DataService getDataService(CompanyConfig companyConfig) throws FMSException {
		
		//set custom config, this should be commented for prod
		Config.setProperty(Config.BASE_URL_QBO, appConfig.getQboUrl());
		
		//create oauth object
		OAuthAuthorizer oauth = new OAuthAuthorizer(appConfig.getConsumerKey(), appConfig.getConsumerSecret(), companyConfig.getAccessToken(), companyConfig.getAccessTokenSecret());
		//create context
		Context context = new Context(oauth, appConfig.getAppToken(), ServiceType.QBO, companyConfig.getRealmId());
		
		//create dataservice
		return new DataService(context);
	}

}
