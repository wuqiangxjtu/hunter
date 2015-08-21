package com.sina.amp.trace.hunter.spring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.http.HttpHunter;
import com.twitter.zipkin.gen.zipkinCoreConstants;

@Component
@Aspect
public class LogAspect {
	
	@Pointcut("(execution(* com.sina.amp.trace.hunter.spring.controller..*.*(..))) || (execution(* com.sina.amp.trace.hunter.spring.service..*.*(..)))" )
	public void pointCut() {
		
	}

	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		HttpHunter.newSpanWithServerRecvAnnotation(joinPoint.getSignature().toString());
	}
	
	@After("pointCut()")
	public void after(JoinPoint joinPoint) {
		HttpHunter.submitServerSendAnnotationAndCollect();
	}
	
//	@Around("pointCut()")
	public void around(ProceedingJoinPoint pjp) throws Throwable { 
		System.out.println(pjp.getSignature() + "  --- around start advice ---");
		try {
			pjp.proceed();
		}catch(Throwable ex) {
			System.out.println("error in around");
			throw ex;
		}
		System.out.println(pjp.getSignature() + "--- around end advice ---");
	}
}
