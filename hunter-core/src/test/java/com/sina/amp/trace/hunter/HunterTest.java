package com.sina.amp.trace.hunter;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.helper.TestSpanCollector;
import com.sina.amp.trace.hunter.service.FirstService;

@Test
public class HunterTest {
	
	FirstService firstService;
	
	public TestSpanCollector spanCollector;
	
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
//		spanCollector = new LoggingSpanCollector();
		spanCollector = new TestSpanCollector();
		ArrayList<TraceFilter> filters = new ArrayList<TraceFilter>();
		filters.add(new FixedSampleRateTraceFilter(1));
		traceFilters = new TraceFilters(filters);
	}
	
	@Test
	public void testStartTrace() {
		Long traceId = 12356L;
		Long parentId = 65321L;
		boolean isSample = false;
		Hunter.startTraceWithParent(ip, port, serviceName, spanCollector, traceFilters, traceId, parentId, isSample);	
	}
	
	@Test
	public void testService1() {
		Hunter.startTraceWithNewTraceId(ip, port, serviceName, spanCollector);
		firstService.serviceA();
		Hunter.endTrace();
		Assert.assertEquals(spanCollector.getSpans().size(), 2);
		Assert.assertEquals(spanCollector.getSpans().get(0).getName(), "serviceB");
		Assert.assertEquals(spanCollector.getSpans().get(1).getName(), "serviceA");
	}
	
	@Test(description="isSample为假的情况下，不跟踪")
	public void testService2() {
		Hunter.startTraceWithParent(ip, port, serviceName, spanCollector, traceFilters, 1L, 11L, false);
		firstService.serviceA();
		Hunter.endTrace();
		Assert.assertEquals(spanCollector.getSpans().size(), 0);
	}
	
	@Test(description="endTrace后调用newSpan等，warning日志中记录了NullpointException，并且被正确捕获")
	public void testService3() {
		Hunter.startTraceWithParent(ip, port, serviceName, spanCollector, 1L, 11L, false);
		Hunter.endTrace();
		firstService.serviceA();
	}
	
	@Test(description="traceId不为空，parentId不为空，isSample为真的情况下，跟踪，并且打印传入的traceId")
	public void testService4() {
		long traceId = 1L;
		long parentId = 11L;
		
		Hunter.startTraceWithParent(ip, port, serviceName, spanCollector, traceId, parentId, true);
		firstService.serviceA();
		Hunter.endTrace();
		
		Assert.assertEquals(spanCollector.getSpans().get(0).getTrace_id(), traceId);
		Assert.assertEquals(spanCollector.getSpans().get(1).getTrace_id(), traceId);
		
		Assert.assertEquals(spanCollector.getSpans().get(0).getParent_id(), spanCollector.getSpans().get(1).getId());
		Assert.assertEquals(spanCollector.getSpans().get(1).getParent_id(), parentId);
	}
	
	@AfterMethod
	public void afterMethod() throws InterruptedException {
		
	}
}
