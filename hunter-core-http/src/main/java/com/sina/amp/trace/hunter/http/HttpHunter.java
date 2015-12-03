package com.sina.amp.trace.hunter.http;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.Hunter;
import com.sina.amp.trace.hunter.TraceFilters;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class HttpHunter extends Hunter {

	private static final Logger LOG = LoggerFactory.getLogger(HttpHunter.class);

	public static final String TRUE = "true";
	public static final String FLASE = "false";

	private static String suffix;

	public static void startTracer(final HttpServletRequest request,
			SpanCollector spanCollector, final TraceFilters traceFilters) {
		try {
			if (request == null) {
				return;
			}
			Long traceId = initTractId(request);
			Long parentId = initParentId(request);
			Boolean isSample = initIsSample(request);

			if (infoComplete(traceId, parentId, isSample)) {
				HttpHunter.startTraceWithParent(request.getLocalAddr(),
						request.getLocalPort(), getServiceName(request),
						spanCollector, traceFilters, traceId, parentId,
						isSample);
			} else {
				HttpHunter.startTraceWithNewTraceId(request.getLocalAddr(),
						request.getLocalPort(), getServiceName(request),
						spanCollector, traceFilters);
			}
			// 在设置traceId前打印的日志与只有默认的traceId, 所以初始化后才打印start日志
			LOG.debug("Start the tracer ......");
		} catch (Exception e) {
			LOG.warn("Start tracer exception:", e);
		}
	}
	
	public static void newSpanWithServerRecvAnnotation(
			final HttpServletRequest request) {
		try {
			HttpHunter.newSpan(request);
			HttpHunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
		} catch (Exception e) {
			LOG.warn("new span with server receive annotation exception:", e);
		}
	}

	private static boolean infoComplete(Long traceId, Long parentId,
			Boolean isSample) {
		return traceId != null && parentId != null && isSample != null;
	}

	private static Boolean initIsSample(final HttpServletRequest request) {
		String headerSample = request.getHeader(HttpHunterHeaders.Sampled
				.getName());
		if (headerSample != null && TRUE.equals(headerSample.trim())) {
			return true;
		} else {
			return null;
		}
	}

	private static Long initParentId(final HttpServletRequest request) {
		Long id = null;
		try {
			if (request.getHeader(HttpHunterHeaders.SpanId.getName()) != null) {
				id = Long.parseLong(request.getHeader(HttpHunterHeaders.SpanId
						.getName()));
			}
		} catch (NumberFormatException e) {
			id = null;
			// 这条日志只有默认的traceId，因为traceId还没有初始化
			LOG.debug("The parent id get from the http header can not parseLong: parentId: "
					+ request.getHeader(HttpHunterHeaders.SpanId.getName()));
		}
		return id;
	}

	private static Long initTractId(final HttpServletRequest request) {
		Long id = null;
		try {
			if (request.getHeader(HttpHunterHeaders.TraceId.getName()) != null) {
				id = Long.parseLong(request.getHeader(HttpHunterHeaders.TraceId
						.getName()));
			}
		} catch (NumberFormatException e) {
			id = null;
			// 这条日志只有默认的traceId，因为traceId还没有初始化
			LOG.debug(
					"The trace id get from the http header can not parseLong: traceId: "
							+ request.getHeader(HttpHunterHeaders.TraceId
									.getName()), e);
		}
		return id;
	}

	private static void newSpan(final HttpServletRequest request) {
		HttpHunter.newSpan(getServiceName(request));
	}

	private static String getServiceName(final HttpServletRequest request) {
		if (suffix != null && !"".equals(suffix)) {
			return request.getRequestURI().substring(0,
				   request.getRequestURI().lastIndexOf(suffix) + suffix.length());
		} else {
			return request.getRequestURI();
		}
	}

	public static void setSuffix(String suffix) {
		HttpHunter.suffix = suffix;
	}
}
