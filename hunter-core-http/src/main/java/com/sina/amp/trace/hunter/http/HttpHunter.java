package com.sina.amp.trace.hunter.http;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.HunterHttpHeaders;
import com.sina.amp.trace.hunter.TraceFilters;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class HttpHunter extends Hunter {

	private static final Logger LOG = LoggerFactory.getLogger(HttpHunter.class);

	public static void startTracer(final HttpServletRequest request,
			SpanCollector spanCollector, final TraceFilters traceFilters) {
		try {
			if (request == null) {
				return;
			}

			Long traceId = null;
			try {
				traceId = Long.parseLong(request
						.getHeader(HunterHttpHeaders.TraceId.getName()));
			} catch (NumberFormatException e) {
				//这条日志只有默认的traceId，因为traceId还没有初始化
				LOG.debug("The trace id get from the http header can not parseLong: traceId: "
						+ request.getHeader(HunterHttpHeaders.TraceId.getName()), e);
			}

			Long parentId = null;
			try {
				parentId = Long.parseLong(request
						.getHeader(HunterHttpHeaders.SpanId.getName()));
			} catch (NumberFormatException e) {
				//这条日志只有默认的traceId，因为traceId还没有初始化
				LOG.debug("The parent id get from the http header can not parseLong: parentId: "
						+ request.getHeader(HunterHttpHeaders.SpanId.getName()));
			}

			if (traceId != null && parentId != null) {
				HttpHunter.startTracer(request.getLocalAddr(),
						request.getLocalPort(), getServiceName(request),
						spanCollector, traceFilters, traceId, parentId);
			} else if (traceId != null && parentId == null) {
				HttpHunter.startTracer(request.getLocalAddr(),
						request.getLocalPort(), getServiceName(request),
						spanCollector, traceFilters, traceId, null);
			} else {
				HttpHunter.startTracer(request.getLocalAddr(),
						request.getLocalPort(), getServiceName(request),
						spanCollector, traceFilters);
			}
			//首先需要设置TracerId，在设置traceId前打印的日志与只有默认的traceId
			LOG.debug("Start the tracer ......");
		}catch(Exception e) {
			LOG.warn("Start tracer exception:", e);
		}


	}

	public static void newSpanWithServerRecvAnnotation(
			final HttpServletRequest request) {
		try {
			HttpHunter.newSpan(request);
			HttpHunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
		}catch(Exception e) {
			LOG.warn("new span with server receive annotation exception:", e);
		}
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
