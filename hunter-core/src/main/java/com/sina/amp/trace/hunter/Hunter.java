package com.sina.amp.trace.hunter;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kristofa.brave.SpanCollector;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class Hunter {
	
	private static final Logger LOG = LoggerFactory.getLogger(Hunter.class);

	public static ThreadLocal<Tracer> TRACER = new ThreadLocal<Tracer>();

	private final static Random RANDOM_GENERATOR = new Random();
	
	protected static final Long NULL_TRACE_ID = 9999999999999L;


	public static void startTracer(String ip, int port, String serviceName,
			SpanCollector spanCollector, final TraceFilters traceFilters) {
		try {
			Hunter.startTracer(ip, port, serviceName, spanCollector, traceFilters,
					RANDOM_GENERATOR.nextLong(),null);
		} catch (Exception e) {
			LOG.warn("start trace exception:" + e);
		}

	}

	protected static void startTracer(String ip, int port, String serviceName,
			SpanCollector spanCollector, final TraceFilters traceFilters,
			Long traceId, Long parentId) {
		try {
			Hunter.TRACER.set(new Tracer(new ThreadState(ip, port, serviceName),
					spanCollector, traceFilters, traceId, parentId));
		} catch (Exception e) {
			LOG.warn("start trace exception:" + e);
		}

	}
	
	public static void newSpanWithServerRecvAnnotation(String spanName) {
		Hunter.newSpan(spanName);
		Hunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
	}
	
	public static void submitServerSendAnnotationAndCollect() {
		Hunter.submitAnnotation(zipkinCoreConstants.SERVER_SEND);
		Hunter.collect();
	}

	public static void newSpan(String spanName) {
		try {
			Hunter.TRACER.get().newSpan(spanName);
		} catch (Exception e) {
			LOG.warn("New Span with spanName:" + spanName + " exception", e);
		}
	}

	/**
	 * 给当前span添加annotation
	 * 
	 * @param annotationName
	 */
	public static void submitAnnotation(String annotationName) {
		try {
			Hunter.TRACER.get().submitAnnotation(annotationName);
		} catch (Exception e) {
			LOG.warn("Submit annotation with name: " + annotationName + " exception", e);
		}
	}

	/**
	 * 利用spanController发送trace log
	 */
	public static void collect() {
		try {
			Hunter.TRACER.get().collect();
		} catch (Exception e) {
			LOG.warn("Collect trace log eception: ",e);
		}
	}

	/**
	 * 清理ThreadLocal
	 */
	public static void endTrace() {
		try {
			Hunter.TRACER.remove();
		} catch (Exception e) {
			LOG.warn("Clear thread local exception:", e);
		}
	}
	
	static Long getTraceId() {
		Long id = NULL_TRACE_ID;
		try {
			id = Hunter.TRACER.get().getTheTraceId();
		} catch (Exception e) {
			//这里不能使用Logger打Log，会引起死循环
		}
		return id;
	}

}
