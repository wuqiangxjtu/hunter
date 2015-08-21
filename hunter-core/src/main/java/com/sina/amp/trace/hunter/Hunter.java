package com.sina.amp.trace.hunter;

import java.util.Random;

import com.github.kristofa.brave.SpanCollector;
import com.twitter.zipkin.gen.zipkinCoreConstants;

public class Hunter {

	public static ThreadLocal<Tracer> TRACER = new ThreadLocal<Tracer>();

	private final static Random RANDOM_GENERATOR = new Random();

	public static void startTracer(String ip, int port, String serviceName,
			SpanCollector spanCollector, final TraceFilters traceFilters) {
		try {
			Hunter.startTracer(ip, port, serviceName, spanCollector, traceFilters,
					RANDOM_GENERATOR.nextLong(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected static void startTracer(String ip, int port, String serviceName,
			SpanCollector spanCollector, final TraceFilters traceFilters,
			Long traceId, Long parentId) {
		try {
			Hunter.TRACER.set(new Tracer(new ThreadState(ip, port, serviceName),
					spanCollector, traceFilters, traceId, parentId));
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	/**
	 * 利用spanController发送trace log
	 */
	public static void collect() {
		try {
			Hunter.TRACER.get().collect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清理ThreadLocal
	 */
	public static void endTrace() {
		try {
			Hunter.TRACER.remove();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static Long getTraceId() {
		Long id = 0L;
		try {
			id =  Hunter.TRACER.get().getTraceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

}
