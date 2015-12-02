package com.sina.amp.trace.hunter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.service.FirstService;

@Test
public class TracerTest {
	
	FirstService firstService;
	
	public SpanCollector spanCollector;
	
	public TraceFilters traceFilters;
	
	String ip;
	int port;
	String serviceName;
	
	@BeforeClass
	public void before() {
		ip = "127.0.0.1";
		port = 8090;
		serviceName = "test-service";
	}
	
	@BeforeMethod
	public void beforeMethod() {
		firstService = new FirstService();
//		spanCollector = new MixSpanCollector("localhost", 9410);
		spanCollector = new LoggingSpanCollector();
		
	}
	
	@Test
	public void testGetTracerWithThreeParams() {
		ThreadState state = new ThreadState(ip, port, serviceName);
		Long traceId = 12345L;
		Tracer tracer = Tracer.getTracer(state, spanCollector, traceId);
		Assert.assertEquals(tracer.getTraceId(), traceId);
		Assert.assertEquals(state.getEndpoint().getService_name(), serviceName);
		Assert.assertEquals(state.getEndpoint().getPort(), port);
	}
	
	@Test(expectedExceptions=RuntimeException.class)
	public void testGetTracerSpanCollectNull() {
		ThreadState state = new ThreadState(ip, port, serviceName);
		Long traceId = 12345L;
		Tracer.getTracer(state, null, traceId);
	}
	
	@Test(expectedExceptions=RuntimeException.class)
	public void testGetTracerTraceIdNull() {
		ThreadState state = new ThreadState(ip, port, serviceName);
		Tracer.getTracer(state, spanCollector, null);
	}

}
