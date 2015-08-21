package com.sina.amp.trace.hunter.logs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.FixedSampleRateTraceFilter;
import com.sina.amp.trace.hunter.HunterHttpHeaders;
import com.sina.amp.trace.hunter.MixSpanCollector;
import com.sina.amp.trace.hunter.TraceFilters;
import com.sina.amp.trace.hunter.http.HttpHunter;
import com.sina.amp.trace.hunter.service.FirstService;

public class HttpHunterTest {
	
	HttpServletRequest request;
	SpanCollector spanCollector;
	TraceFilters traceFilters;
	FirstService firstService;
	
	@Before
	public void before() {
		//Mock
		request = mock(HttpServletRequest.class);
		
		List<TraceFilter> traceList = new ArrayList<TraceFilter>();
		traceList.add(new FixedSampleRateTraceFilter(1));
		traceFilters = new TraceFilters(traceList);
		spanCollector = new MixSpanCollector("localhost",9410);
		when(request.getHeader(HunterHttpHeaders.TraceId.getName())).thenReturn("5415072796348909484");
//		when(request.getHeader(HunterHttpHeaders.SpanId.getName())).thenReturn("5267450487794558941");
		when(request.getRequestURI()).thenReturn("/hunter/http/test");
		when(request.getLocalAddr()).thenReturn("127.0.0.1");
		when(request.getLocalPort()).thenReturn(8000);
		HttpHunter.startTracer(request, spanCollector, traceFilters);
		firstService = new FirstService();
	}
	

	@Test
	public void testTraceId() {
		firstService.serviceA();
	}
	
	@After
	public void after() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HttpHunter.endTrace();
	}
	

}
