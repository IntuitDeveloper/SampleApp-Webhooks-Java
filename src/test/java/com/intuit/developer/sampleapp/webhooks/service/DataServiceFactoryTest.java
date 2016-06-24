package com.intuit.developer.sampleapp.webhooks.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.intuit.developer.sampleapp.webhooks.domain.AppConfig;
import com.intuit.developer.sampleapp.webhooks.domain.CompanyConfig;
import com.intuit.developer.sampleapp.webhooks.service.qbo.DataServiceFactory;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.security.OAuthAuthorizer;
import com.intuit.ipp.services.DataService;

/**
 * @author dderose
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class)
public class DataServiceFactoryTest {
	
	DataServiceFactory dataServiceFactory;
    DataService dataService;
    OAuthAuthorizer oAuthAuthorizer;
    Context context;
    AppConfig appConfig;
    
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		dataServiceFactory = new DataServiceFactory();
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
	}
	
	@Test
	public void testGetDataService() throws Exception {
        final String accessToken = "accessToken";
        final String accessTokenSecret = "accessTokenSecret";
        final String realmId = "1234567";
        
		CompanyConfig c = new CompanyConfig(realmId, accessToken, accessTokenSecret, null);
		
        DataService ds = dataServiceFactory.getDataService(c);
		Assert.assertNotNull(ds);
	}
	


}
