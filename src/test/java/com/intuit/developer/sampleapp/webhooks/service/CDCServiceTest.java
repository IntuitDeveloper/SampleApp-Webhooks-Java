package com.intuit.developer.sampleapp.webhooks.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;

import com.intuit.developer.sampleapp.webhooks.domain.AppConfig;
import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.service.qbo.CDCService;
import com.intuit.developer.sampleapp.webhooks.service.qbo.DataServiceFactory;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.security.OAuthAuthorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.DateUtils;

/**
 * @author dderose
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class)
public class CDCServiceTest {

	private CDCService cdcService;
	
	@Autowired
	protected WebApplicationContext wac;
	
	DataServiceFactory dataServiceFactory;
    DataService dataService;
    OAuthAuthorizer oAuthAuthorizer;
    Context context;
    AppConfig appConfig;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		cdcService = Mockito.mock(CDCService.class);
		dataServiceFactory = Mockito.mock(DataServiceFactory.class);
		ReflectionTestUtils.setField(cdcService, "dataServiceFactory", dataServiceFactory);
		
		dataService = Mockito.mock(DataService.class);
		oAuthAuthorizer = Mockito.mock(OAuthAuthorizer.class);
		context = Mockito.mock(Context.class);
		appConfig = Mockito.mock(AppConfig.class);
		
		final String consumerKey = "consumerKey";
        final String consumerSecret = "consumerSecret";
        final String appToken = "appToken";
        final String qboUrl = "qbourl";
        
        Mockito.when(appConfig.getConsumerKey()).thenReturn(consumerKey);
        Mockito.when(appConfig.getConsumerSecret()).thenReturn(consumerSecret);
        Mockito.when(appConfig.getAppToken()).thenReturn(appToken);
        Mockito.when(appConfig.getQboUrl()).thenReturn(qboUrl);
        ReflectionTestUtils.setField(dataServiceFactory, "appConfig", appConfig);
  	
		CompanyConfig companyConfig = new CompanyConfig();
		companyConfig.setLastCdcTimestamp(DateUtils.getStringFromDateTime(DateUtils.getDateWithPrevDays(2)));
		Mockito.when(companyConfig.getWebhooksSubscribedEntites()).thenReturn("Customer");
		
		Mockito.when(dataServiceFactory.getDataService(companyConfig)).thenReturn(dataService);

	}
	
	@Test
	public void callDataService() throws Exception {
		
		CompanyConfig companyConfig = new CompanyConfig();
		companyConfig.setLastCdcTimestamp(DateUtils.getStringFromDateTime(DateUtils.getDateWithPrevDays(2)));
		companyConfig.setWebhooksSubscribedEntites("Customer");
		
		Mockito.doNothing().doThrow(new RuntimeException()).when(cdcService).callDataService(companyConfig);
		
	}
}
