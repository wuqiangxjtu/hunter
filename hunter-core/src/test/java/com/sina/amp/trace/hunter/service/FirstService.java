package com.sina.amp.trace.hunter.service;

import org.apache.log4j.Logger;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.TraceFilters;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class FirstService {
	
	Logger log = Logger.getLogger(FirstService.class);
	


	public void serviceA() {
		log.info("-----------serviceA-----------");
		Hunter.newSpanWithServerRecvAnnotation("serviceA");		
		serviceB();
		Hunter.submitServerSendAnnotationAndCollect();
	}
	
	public void serviceB() {
		log.info("-----------serviceB-----------");
		Hunter.newSpanWithServerRecvAnnotation("serviceB");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Hunter.submitServerSendAnnotationAndCollect();
	}
}
