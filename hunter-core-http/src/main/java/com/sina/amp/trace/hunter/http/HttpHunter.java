package com.sina.amp.trace.hunter.http;

import javax.servlet.http.HttpServletRequest;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.HunterHttpHeaders;
import com.sina.amp.trace.hunter.TraceFilters;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class HttpHunter extends Hunter {

	
	public static void startTracer(final HttpServletRequest request,
			SpanCollector spanCollector, final TraceFilters traceFilters) {
		if (request == null) {
			return;
		}
		
		Long traceId = null;
		try {
			traceId = Long.parseLong(request.getHeader(HunterHttpHeaders.TraceId
					.getName()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		
		Long parentId = null;
		try {
			parentId = Long.parseLong(request.getHeader(HunterHttpHeaders.SpanId
					.getName()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		if (traceId != null  && parentId != null) {
			HttpHunter.startTracer(request.getLocalAddr(), request.getLocalPort(),
					getServiceName(request), spanCollector, traceFilters,
					traceId, parentId);
		} else if(traceId != null && parentId == null) {
			HttpHunter.startTracer(request.getLocalAddr(), request.getLocalPort(),
					getServiceName(request), spanCollector, traceFilters,
					traceId, null);
		}else {
			HttpHunter.startTracer(request.getLocalAddr(), request.getLocalPort(),
					getServiceName(request), spanCollector, traceFilters);
		}

	}

	public static void newSpanWithServerRecvAnnotation(final HttpServletRequest request) {
		HttpHunter.newSpan(request);
		HttpHunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
	}


	
	/* 
	 * @param request
	 */
	public static void newSpan(final HttpServletRequest request) {
		HttpHunter.newSpan(getServiceName(request));
	}

	private static String getServiceName(final HttpServletRequest request) {
		return request.getRequestURI();
	}
}
