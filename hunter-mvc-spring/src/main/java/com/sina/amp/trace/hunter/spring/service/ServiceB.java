package com.sina.amp.trace.hunter.spring.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ServiceB {
	
	public void method1() {
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("ServiceB-Method1 called.");
	}
	
	public void method2() {
		System.out.println("ServiceB-Method2 called.");
		method3();
	}

	public void method3() {
		System.out.println("ServiceB-Method3 called.");
	}
}
