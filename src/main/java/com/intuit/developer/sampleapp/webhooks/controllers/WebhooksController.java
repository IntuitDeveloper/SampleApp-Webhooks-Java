package com.intuit.developer.sampleapp.webhooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.developer.sampleapp.webhooks.domain.ResponseWrapper;
import com.intuit.developer.sampleapp.webhooks.service.queue.QueueService;
import com.intuit.developer.sampleapp.webhooks.service.security.SecurityService;
import com.intuit.ipp.util.Logger;
import com.intuit.ipp.util.StringUtils;

/**
 * Controller class for the webhooks endpoint
 * 
 * @author dderose
 *
 */
@RestController
public class WebhooksController {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();

	private static final String SIGNATURE = "intuit-signature";
	private static final String SUCCESS = "Success";
	private static final String ERROR = "Error";
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	private QueueService queueService;
  
    /**
     * Method to receive webhooks event notification 
     * 1. Validates payload
     * 2. Adds it to a queue
     * 3. Sends success response back
     * 
     * Note: Queue processing occurs in an async thread
     * 
     * @param signature
     * @param payload
     * @return
     */
    @RequestMapping(value = "/webhooks", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ResponseWrapper> webhooks(@RequestHeader(SIGNATURE) String signature, @RequestBody String payload) {
    	
    	// if signature is empty return 401
    	if (!StringUtils.hasText(signature)) {
    		return new ResponseEntity<>(new ResponseWrapper(ERROR), HttpStatus.FORBIDDEN);
    	}
    	
    	// if payload is empty, don't do anything
    	if (!StringUtils.hasText(payload)) {
    		new ResponseEntity<>(new ResponseWrapper(SUCCESS), HttpStatus.OK);
    	}
    	
    	LOG.info("request recieved ");
		
		//if request valid - push to queue
		if (securityService.isRequestValid(signature, payload)) {
			queueService.add(payload);
		} else {
			return new ResponseEntity<>(new ResponseWrapper(ERROR), HttpStatus.FORBIDDEN);
		}
		
		LOG.info("response sent ");
    	return new ResponseEntity<>(new ResponseWrapper(SUCCESS), HttpStatus.OK);
    }

}
