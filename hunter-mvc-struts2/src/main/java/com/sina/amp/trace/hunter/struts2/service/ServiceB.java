package com.sina.amp.trace.hunter.struts2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ServiceB {
	
	Logger LOG = LoggerFactory.getLogger(ServiceB.class);
	
	public void method1() {
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		LOG.info("ServiceB-Method1 called.");
	}
	
	public void method2() {
		LOG.info("ServiceB-Method2 called.");
		method3();
	}

	public void method3() {
		LOG.info("ServiceB-Method3 called.");
	}
}
