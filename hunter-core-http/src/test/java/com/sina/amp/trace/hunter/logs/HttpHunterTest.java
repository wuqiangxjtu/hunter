package com.sina.amp.trace.hunter.logs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.FixedSampleRateTraceFilter;
import com.sina.amp.trace.hunter.MixSpanCollector;
import com.sina.amp.trace.hunter.TraceFilters;
import com.sina.amp.trace.hunter.helper.TestSpanCollector;
import com.sina.amp.trace.hunter.http.HttpHunter;
import com.sina.amp.trace.hunter.service.FirstService;
import com.sina.amp.trace.hunter.service.LocalService;

public class HttpHunterTest {
	
	HttpServletRequest request;
	HttpServletRequest request_remote;
	TestSpanCollector spanCollector;
	TraceFilters traceFilters;
	FirstService firstService;
	
	@BeforeMethod
	public void before() {
		//Mock
		request = mock(HttpServletRequest.class);
		request_remote = mock(HttpServletRequest.class);
		
		List<TraceFilter> traceList = new ArrayList<TraceFilter>();
		traceList.add(new FixedSampleRateTraceFilter(1));
		traceFilters = new TraceFilters(traceList);
		
//		spanCollector = new MixSpanCollector("localhost",9410);
		spanCollector = new TestSpanCollector();
		
	}
	

	@Test
	public void testTraceWithNoParent() {

		when(request.getRequestURI()).thenReturn("/hunter/http/test111");
		when(request.getLocalAddr()).thenReturn("127.0.0.1");
		when(request.getLocalPort()).thenReturn(8000);
		HttpHunter.startTracer(request, spanCollector, traceFilters);
		firstService = new FirstService();
		firstService.serviceA();
	}
	
	@Test
	public void testTraceWithParent() {
//		when(request.getHeader(HunterHttpHeaders.TraceId.getName())).thenReturn("5415072796348909485");
//		when(request.getHeader(HunterHttpHeaders.SpanId.getName())).thenReturn("5267450487794558943");
		
		when(request_remote.getRequestURI()).thenReturn("/hunter/http/test222");
		when(request_remote.getLocalAddr()).thenReturn("5.6.7.8");
		when(request_remote.getLocalPort()).thenReturn(8000);
		
		
		
	}
	
	@Test
	public void testWithActionSuffix1() {
		
		
//		when(request.getHeader(HunterHttpHeaders.TraceId.getName())).thenReturn("5415072796348909485");
//		when(request.getHeader(HunterHttpHeaders.SpanId.getName())).thenReturn("5267450487794558943");
		when(request.getRequestURI()).thenReturn("/hunter/http/test111.action;tewltjljdslfjsdljfldsjflsj");
		when(request.getLocalAddr()).thenReturn("127.0.0.1");
		when(request.getLocalPort()).thenReturn(8000);
		HttpHunter.setSuffix(".action");
		HttpHunter.startTracer(request, spanCollector, traceFilters);
		firstService = new FirstService();
		firstService.serviceA();
	}
	
	@Test
	public void testWithActionSuffix2() {
		
//		when(request.getHeader(HunterHttpHeaders.TraceId.getName())).thenReturn("5415072796348909485");
//		when(request.getHeader(HunterHttpHeaders.SpanId.getName())).thenReturn("5267450487794558943");
		when(request.getRequestURI()).thenReturn("/hunter/http/test111.action;tewltjljdslfjsdljfldsjflsj");
		when(request.getLocalAddr()).thenReturn("127.0.0.1");
		when(request.getLocalPort()).thenReturn(8000);
//		HttpHunter.setSuffix(".action");
		HttpHunter.startTracer(request, spanCollector, traceFilters);
		firstService = new FirstService();
		firstService.serviceA();
	}
	
	@AfterMethod
	public void after() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HttpHunter.endTrace();
	}
	

}
