package com.intuit.developer.sampleapp.webhooks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.service.CompanyConfigService;
import com.intuit.developer.sampleapp.webhooks.service.qbo.QBODataService;
import com.intuit.ipp.util.DateUtils;
import com.intuit.ipp.util.Logger;

/**
 * @author dderose
 *
 */
@Component
@Configuration
@PropertySource(value="classpath:/application.properties", ignoreResourceNotFound=true)
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	@Autowired
    private Environment env;
	
	@Autowired
	private CompanyConfigService companyConfigService;
	
	@Autowired
	@Qualifier("QueryAPI")
	private QBODataService queryService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		// Load CompanyConfig table with realmIds and access tokens
		loadCompanyConfig();
		
		//get list of companyConfigs
		Iterable<CompanyConfig> companyConfigs = companyConfigService.getAllCompanyConfigs();
		
		//run findQuery for all entities for each realmId
		// and update the timestamp in the database
		// This is done so that the app is synced before it listens to event notifications
		for (CompanyConfig config : companyConfigs) {
			try {
				String lastQueryTimestamp = DateUtils.getStringFromDateTime(DateUtils.getCurrentDateTime());
				queryService.callDataService(config);
				
				//update timestamp data in table
				config.setLastCdcTimestamp(lastQueryTimestamp);
				companyConfigService.save(config);
				
			} catch (Exception ex) {
				LOG.error("Error loading company configs" , ex.getCause());
			}
		}
			
	}

	/**
	 * Read access tokens and other properties from the configuration file and load it in the in-memory h2 database
	 * 
	 */
	private void loadCompanyConfig() {
		final CompanyConfig companyConfig = new CompanyConfig(env.getProperty("company1.id"), env.getProperty("company1.accessToken"), env.getProperty("company1.accessTokenSecret"), env.getProperty("company1.webhooks.subscribed.entities"));
		companyConfigService.save(companyConfig);
			
		final CompanyConfig company2 = new CompanyConfig(env.getProperty("company2.id"), env.getProperty("company2.accessToken"), env.getProperty("company2.accessTokenSecret"), env.getProperty("company2.webhooks.subscribed.entities"));
		companyConfigService.save(company2);
	}

}
