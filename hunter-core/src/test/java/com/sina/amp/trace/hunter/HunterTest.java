package com.sina.amp.trace.hunter;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.service.FirstService;

public class HunterTest {
	
	FirstService firstService;
	
	public SpanCollector spanCollector;
	
	public TraceFilters traceFilters;

	@Before
	public void before() {
		firstService = new FirstService();
		spanCollector = new MixSpanCollector("localhost", 9410);
		Hunter.startTracer("127.0.0.1", 8090, "test-service", spanCollector, null);
		
	}
	
	@Test
	public void testFirstService() {
		firstService.serviceA();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearUp() throws InterruptedException {

		Hunter.endTrace();
	}
}
