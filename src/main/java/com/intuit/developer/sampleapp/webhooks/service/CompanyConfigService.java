package com.intuit.developer.sampleapp.webhooks.service;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;

/**
 * @author dderose
 *
 */
public interface CompanyConfigService {

	public Iterable<CompanyConfig> getAllCompanyConfigs();
	
	public CompanyConfig getCompanyConfigByRealmId(String realmId);
	
	public CompanyConfig getCompanyConfigById(Integer id);

	public void save(CompanyConfig companyConfig);
}
