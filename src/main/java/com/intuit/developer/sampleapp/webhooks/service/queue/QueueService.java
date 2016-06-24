package com.intuit.developer.sampleapp.webhooks.service.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.ipp.util.Logger;

/**
 * Manages a queue and executes a single async thread to process the queue whenever an item is added to the queue
 * @author dderose
 *
 */
@Service
public class QueueService {
	
	private static final org.slf4j.Logger LOG = Logger.getLogger();
	
	private static final BlockingQueue<String> QUEUE =  new LinkedBlockingQueue<>();
	
	@Autowired
	private QueueProcessor queueProcessor;
	
	private ExecutorService executorService;
	
	@PostConstruct
	public void init() {
		// intitialize a single thread executor, this will ensure only one thread processes the queue
		executorService = Executors.newSingleThreadExecutor();
	}
	
	public void add(String payload) {
		
		// add payload to the queue
		QUEUE.add(payload);
		LOG.info("added to queue:::: queue size " + QUEUE.size());
		
		//Call executor service
		executorService.submit(queueProcessor);				
	}
	
	public BlockingQueue<String> getQueue() {
		return QUEUE;
	}

	@PreDestroy
	public void shutdown() {
		executorService.shutdown();
	}

}
