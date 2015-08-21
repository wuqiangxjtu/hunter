package com.sina.amp.trace.hunter.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.TraceFilters;
import com.sina.amp.trace.hunter.http.HttpHunter;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class MonitorHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	SpanCollector spanCollector;
	
	@Autowired
	TraceFilters traceFilters;

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		try{
			HttpHunter.submitServerSendAnnotationAndCollect();
		}finally {
			HttpHunter.endTrace();
		}
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		Boolean b =  super.preHandle(request, response, handler);
		if(b) {
			HttpHunter.startTracer(request, spanCollector, traceFilters);
			HttpHunter.newSpanWithServerRecvAnnotation(request);
//			Thread.sleep(1000);
		}

		return b;
	}





	

}
