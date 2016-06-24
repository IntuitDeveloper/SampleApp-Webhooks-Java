package com.intuit.developer.sampleapp.webhooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.service.CompanyConfigService;

/**
 * Controller class for company config enpoints
 * @author dderose
 *
 */
@RestController
public class CompanyController {
	
	@Autowired
	private CompanyConfigService companyConfigService;
    
    @RequestMapping(value = "/companyConfigs", method = RequestMethod.GET)
    public ResponseEntity<Iterable<CompanyConfig>> listAllRealmConfigs() {
    	
    	Iterable<CompanyConfig> log = companyConfigService.getAllCompanyConfigs();
    	
    	return new ResponseEntity<>(log, HttpStatus.OK);
    }
    
}
