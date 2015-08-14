package com.sina.amp.trace.hunter.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.TraceFilters;
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
			Hunter.submitAnnotation(zipkinCoreConstants.SERVER_SEND);
			Hunter.collect();
		}finally {
			Hunter.clear();
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
			Hunter.beginTrace(request, spanCollector,traceFilters);
			Hunter.newSpan(request);
			Hunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
//			Thread.sleep(1000);
		}

		return b;
	}





	

}
