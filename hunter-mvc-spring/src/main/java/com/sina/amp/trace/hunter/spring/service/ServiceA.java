package com.sina.amp.trace.hunter.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ServiceA {
	@Autowired
	ServiceB serviceB;
	
	public void method1() {
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("ServiceA-Method1 called.");
//		method2();
		serviceB.method1();
//		serviceB.method2();
	}
	
	public void method2() {
		System.out.println("ServiceA-Method2 called.");
	}
	
	public void method3() {
		System.out.println("ServiceA-Method3 called.");
	}

}
