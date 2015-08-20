package com.sina.amp.trace.hunter.service;

import org.apache.log4j.Logger;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.TraceFilters;

public class FirstService {
	
	Logger log = Logger.getLogger(FirstService.class);
	
	public SpanCollector spanCollector;
	
	public TraceFilters traceFilters;

	public void serviceA() {
		String serviceName = "/serviceA";
		Hunter.startTracer("localhost", 8080, "testServicebbb", spanCollector, null);
		log.info("-----------serviceA-----------");
		Hunter.newSpan(serviceName);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Hunter.submitAnnotation("begin");
		
//		serviceB();
		
		Hunter.submitAnnotation("end");
		Hunter.collect();
//		Hunter.endTrace();
	}
	
	public void serviceB() {
		String serviceName = "/serviceA";
		log.info("-----------serviceB-----------");
		Hunter.newSpan(serviceName);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Hunter.submitAnnotation("begin");
		Hunter.submitAnnotation("end");
		Hunter.collect();
	}
	
}
