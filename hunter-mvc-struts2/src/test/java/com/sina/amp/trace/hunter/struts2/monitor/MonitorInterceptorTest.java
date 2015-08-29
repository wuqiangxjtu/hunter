package com.sina.amp.trace.hunter.struts2.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.sina.amp.trace.hunter.struts2.base.SpringTestNGTestCase;
import com.sina.amp.trace.hunter.struts2.service.ServiceA;

public class MonitorInterceptorTest extends SpringTestNGTestCase{
	
	@Autowired
	ServiceA serviceA;
	
	@Test
	public void testMonitor1() {
		serviceA.method1();
		System.out.println("aaa");
	}
}
