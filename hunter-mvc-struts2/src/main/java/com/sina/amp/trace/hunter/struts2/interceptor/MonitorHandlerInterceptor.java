package com.sina.amp.trace.hunter.struts2.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;








import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.kristofa.brave.SpanCollector;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.sina.amp.trace.hunter.TraceFilters;
import com.sina.amp.trace.hunter.http.HttpHunter;

public class MonitorHandlerInterceptor extends  MethodFilterInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	SpanCollector spanCollector;
	
	@Autowired
	TraceFilters traceFilters;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		String result = null;
		try {
			ActionContext actionContext = invocation.getInvocationContext();
			HttpServletRequest request = (HttpServletRequest) actionContext
					.get(StrutsStatics.HTTP_REQUEST);
			HttpHunter.startTracer(request, spanCollector, traceFilters);
			HttpHunter.newSpanWithServerRecvAnnotation(request);
			
			result = invocation.invoke();
			
			HttpHunter.submitServerSendAnnotationAndCollect();
		}finally {
			HttpHunter.endTrace();
		}
		return result;
	}







	

}
