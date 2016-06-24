package com.intuit.developer.sampleapp.webhooks.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Entity to store app configs
 * 
 * @author dderose
 *
 */
@Configuration
@PropertySource(value="classpath:/application.properties", ignoreResourceNotFound=true)
public class AppConfig {
	
	@Autowired
    Environment env;

	public String getAppToken() {
		return env.getProperty("app.token");
	}

	public String getConsumerKey() {
		return env.getProperty("consumer.key");
	}

	public String getConsumerSecret() {
		return env.getProperty("consumer.secret");
	}

	public String getQboUrl() {
		return env.getProperty("qbo.url");
	}

	
}
