package com.sina.amp.trace.hunter;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.service.FirstService;

public class HunterTest {
	
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
	
	
	@Test(description="traceId, parentId,isSample都是null, 日志正常，并且打印的traceId是非默认traceId")
	public void testService1() {
		Hunter.startTracer(ip, port, serviceName, spanCollector, null);
		firstService.serviceA();
		Hunter.endTrace();
	}
	
	@Test(description="traceId不为空，parentId不为空，isSample为假的情况下，不跟踪")
	public void testService2() {
		Hunter.startTracer(ip, port, serviceName, spanCollector, null, 1L, 11L, false);
		firstService.serviceA();
		Hunter.endTrace();
	}
	
	@Test(description="endTrace后调用newSpan等，warning日志中记录了NullpointException，并且被正确捕获")
	public void testService3() {
		Hunter.startTracer(ip, port, serviceName, spanCollector, null, 1L, 11L, false);
//		firstService.serviceA();
		Hunter.endTrace();
		firstService.serviceA();
	}
	
	@Test(description="traceId不为空，parentId不为空，isSample为真的情况下，跟踪，并且打印传入的traceId")
	public void testService4() {
		Hunter.startTracer(ip, port, serviceName, spanCollector, null, 1L, 11L, true);
		firstService.serviceA();
		Hunter.endTrace();
	}
	
	@AfterMethod
	public void afterMethod() throws InterruptedException {
		
	}
}
