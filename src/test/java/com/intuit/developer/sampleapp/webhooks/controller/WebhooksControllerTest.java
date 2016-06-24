package com.intuit.developer.sampleapp.webhooks.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.sampleapp.webhooks.controllers.WebhooksController;
import com.intuit.developer.sampleapp.webhooks.service.queue.QueueService;
import com.intuit.developer.sampleapp.webhooks.service.security.SecurityService;

/**
 * @author dderose
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class)
public class WebhooksControllerTest {
	
	private MockMvc mockMvc;
	private WebhooksController webhooksController;
	private SecurityService securityServiceMock;
	private QueueService queueServiceMock;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


	
	@Autowired
	protected WebApplicationContext wac;
	
	@Before
	public void setUp() throws Exception {
		mockMvc = webAppContextSetup(wac).alwaysExpect(status().isOk()).build();
		MockitoAnnotations.initMocks(this);
		
		webhooksController = new WebhooksController();
		securityServiceMock = Mockito.mock(SecurityService.class);
		ReflectionTestUtils.setField(webhooksController, "securityService", securityServiceMock);
		
		queueServiceMock = Mockito.mock(QueueService.class);
		ReflectionTestUtils.setField(webhooksController, "queueService", queueServiceMock);
		
		Mockito.when(securityServiceMock.isRequestValid(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

		Mockito.doNothing().doThrow(new RuntimeException()).when(queueServiceMock).add(Mockito.anyString());
		
		mockMvc = MockMvcBuilders.standaloneSetup(webhooksController).build();
		
	}

	@Test
	public void webhooks() throws Exception {
		
		String payload = "{}";
		
		mockMvc.perform(post("/webhooks")
		.contentType(APPLICATION_JSON_UTF8)
		.content(convertObjectToJsonBytes(payload))
		.header("intuit-signature", "1234"))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void webhooksWithWrongSignature() throws Exception {
		
		String payload = "{}";
		
		mockMvc.perform(post("/webhooks")
		.contentType(APPLICATION_JSON_UTF8)
		.content(convertObjectToJsonBytes(payload))
		.header("intuit-signature", ""))
		.andExpect(status().isForbidden());
		
	}
	
	public  byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
