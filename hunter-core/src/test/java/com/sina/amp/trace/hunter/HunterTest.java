package com.sina.amp.trace.hunter;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.service.FirstService;

public class HunterTest {
	
	FirstService firstService;

	@Before
	public void before() {
		firstService = new FirstService();
//		firstService.spanCollector = new LoggingSpanCollector();
		firstService.spanCollector = new MixSpanCollector("localhost", 9410);
		List<TraceFilter> list = new ArrayList<TraceFilter>();
		list.add(new FixedSampleRateTraceFilter(1));
		firstService.traceFilters = new TraceFilters(list);
	}
	
	@Test
	public void testFirstService() {
		firstService.serviceA();
	}
	
	@After
	public void tearUp() throws InterruptedException {
		Thread.sleep(5000);
	}
}
