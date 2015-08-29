package com.sina.amp.trace.hunter.struts2.monitor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

@Component
public class MonitorInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		System.out.println(invocation.getMethod().getClass().getName());
		System.out.println(invocation.getMethod().getClass().getPackage().getName());
		return null;
	}

}
