package com.intuit.developer.sampleapp.webhooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.repository.CompanyConfigRepository;
import com.intuit.developer.sampleapp.webhooks.service.security.SecurityService;
import com.intuit.ipp.util.Logger;

/**
 * Service class to store and retrieve CompanyConfig data from database
 * During save access token and secret is encrypted
 * During retrieve the tokens are decrypted
 * 
 * @author dderose
 *
 */
@Service
public class CompanyConfigServiceImpl implements CompanyConfigService {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	@Autowired
	private CompanyConfigRepository companyConfigRepository;
	
	@Autowired
	private SecurityService securityService;

	@Override
	public Iterable<CompanyConfig> getAllCompanyConfigs() {
		Iterable<CompanyConfig> companyConfigs = companyConfigRepository.findAll();
		try {
			companyConfigs.forEach(config -> config.setAccessToken(decrypt(config.getAccessToken())));
			companyConfigs.forEach(config -> config.setAccessTokenSecret(decrypt(config.getAccessTokenSecret())));
		} catch (Exception ex) {
			LOG.error("Error loading company configs" , ex.getCause());
		}
		return companyConfigs;
	}

	@Override
	public CompanyConfig getCompanyConfigByRealmId(String realmId) {
		CompanyConfig companyConfig = companyConfigRepository.findByRealmId(realmId);
		try {
			companyConfig.setAccessToken(decrypt(companyConfig.getAccessToken()));
			companyConfig.setAccessTokenSecret(decrypt(companyConfig.getAccessTokenSecret()));
			return companyConfig;
		} catch (Exception ex) {
			LOG.error("Error loading company config" , ex.getCause());
			return null;
		}
		
	}
	
	@Override
	public CompanyConfig getCompanyConfigById(Integer id) {
		CompanyConfig companyConfig = companyConfigRepository.findOne(id);
		try {
			companyConfig.setAccessToken(decrypt(companyConfig.getAccessToken()));
			companyConfig.setAccessTokenSecret(decrypt(companyConfig.getAccessTokenSecret()));
			return companyConfig;
		} catch (Exception ex) {
			LOG.error("Error loading company config" , ex.getCause());
			return null;
		}
		
	}
	
	@Override
	public void save(CompanyConfig companyConfig) {
		try {
			companyConfig.setAccessToken(encrypt(companyConfig.getAccessToken()));
			companyConfig.setAccessTokenSecret(encrypt(companyConfig.getAccessTokenSecret()));
			companyConfigRepository.save(companyConfig);
		} catch (Exception ex) {
			LOG.error("Error loading company config" , ex.getCause());
		}
		
	}
	
	public String decrypt(String string) {
		try {
			return securityService.decrypt(string);
		} catch (Exception ex) {
			LOG.error("Error decrypting" , ex.getCause());
			return null;
		}
	}
	
	public String encrypt(String string) {
		try {
			return securityService.encrypt(string);
		} catch (Exception ex) {
			LOG.error("Error encrypting" , ex.getCause());
			return null;
		}
	}

}
