package com.intuit.developer.sampleapp.webhooks.repository;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dderose
 *
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.intuit.developer.sampleapp.webhooks.domain"})
@EnableJpaRepositories(basePackages = {"com.intuit.developer.sampleapp.webhooks.repository"})
@EnableTransactionManagement
public class PersistenceConfiguration {

}
