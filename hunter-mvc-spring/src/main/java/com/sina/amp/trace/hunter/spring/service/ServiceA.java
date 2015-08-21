package com.sina.amp.trace.hunter.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceA {
	
	Logger LOG = LoggerFactory.getLogger(ServiceA.class);
	
	@Autowired
	ServiceB serviceB;
	
	public void method1() {
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		LOG.info("ServiceA-Method1 called.");
//		method2();
		serviceB.method1();
//		serviceB.method2();
	}
	
	public void method2() {
		LOG.info("ServiceA-Method2 called.");
	}
	
	public void method3() {
		LOG.info("ServiceA-Method3 called.");
	}

}
