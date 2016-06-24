package com.intuit.developer.sampleapp.webhooks.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.WebApplicationContext;

import com.intuit.developer.sampleapp.webhooks.service.security.SecurityService;

/**
 * @author dderose
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class)
public class SecurityServiceTest {
	
	SecurityService securityService = new SecurityService();
	
	@Autowired
	protected WebApplicationContext wac;
	
	Environment env;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		env = Mockito.mock(Environment.class);
		
		final String key = "12345";
        Mockito.when(env.getProperty("webhooks.verifier.token")).thenReturn(key);
        Mockito.when(env.getProperty("encryption.key")).thenReturn(key);

        ReflectionTestUtils.setField(securityService, "env", env);
        
	}

	@Test
	public void testValidRequest() {
		
		String expected = "gfTuE5fLxnstOq8ln4LNWZfn24cg8FVB6PBv455lfzg=";
		boolean result = securityService.isRequestValid("12345", "abcd");
		Assert.assertFalse(result);
		result = securityService.isRequestValid(expected, "abcd");
		Assert.assertTrue(result);
	}

}
