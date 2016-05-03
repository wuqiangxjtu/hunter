package com.sina.amp.trace.hunter;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.reporters.util.StackTraceTools;

import com.github.kristofa.brave.SpanCollector;
import com.sina.amp.trace.hunter.utils.StackTraceUtil;
import com.twitter.zipkin.gen.zipkinCoreConstants;

/**
 * 对外暴露，通过该类可以完成以下工作： 1. 设置开始 2. 设置完成 3. 添加span 4. 添加annotation
 * 
 * @author wuqiang
 *
 */
public class Hunter {

	private static final Logger LOG = LoggerFactory.getLogger(Hunter.class);

	// 每个线程的调用数据用Tracer保存，各个线程之前不能相互影响，所以保存在ThreadLocal变量中
	public static ThreadLocal<Tracer> TRACER = new ThreadLocal<Tracer>();

	private final static Random RANDOM_GENERATOR = new Random();

	// 当Trace id不存在时的默认id
	protected static final Long NULL_TRACE_ID = 9999999999999L;

	public static void startTraceWithNewTraceId(String ip, int port,
			String serviceName, SpanCollector spanCollector) {
		try {
			Hunter.TRACER.set(Tracer.getTracer(new ThreadState(ip, port,
					serviceName), spanCollector, RANDOM_GENERATOR
					.nextLong()));
		} catch (Exception e) {
			LOG.warn("start trace exception:" + StackTraceUtil.getStackTrace(e));
		}

	}
	
	public static void startTraceWithNewTraceId(String ip, int port,
			String serviceName, SpanCollector spanCollector,
			final TraceFilters traceFilters) {
		try {
			Hunter.TRACER.set(Tracer.getTracer(new ThreadState(ip, port,
					serviceName), spanCollector, traceFilters, RANDOM_GENERATOR
					.nextLong()));
		} catch (Exception e) {
			LOG.warn("start trace exception:" + StackTraceUtil.getStackTrace(e));
		}
	}
	
	protected static void startTraceWithParent(String ip, int port, String serviceName,
			SpanCollector spanCollector, Long traceId, Long parentId, Boolean isSample) {
		try {
			Hunter.TRACER.set(Tracer.getTracer(new ThreadState(ip, port,
					serviceName), spanCollector, traceId, parentId, isSample));
		} catch (Exception e) {
			LOG.warn("start trace exception:" + StackTraceUtil.getStackTrace(e));
		}
	}

	protected static void startTraceWithParent(String ip, int port, String serviceName,
			SpanCollector spanCollector, final TraceFilters traceFilters,
			Long traceId, Long parentId, Boolean isSample) {
		try {
			Hunter.TRACER.set(Tracer.getTracer(new ThreadState(ip, port,
					serviceName), spanCollector, traceFilters, traceId, parentId, isSample));
		} catch (Exception e) {
			LOG.warn("start trace exception:" + StackTraceUtil.getStackTrace(e));
		}
	}
	
	protected static void startTraceWithTraceIdForTest(String ip, int port, String serviceName,
			SpanCollector spanCollector, Long traceId) {
		Hunter.TRACER.set(Tracer.getTracer(new ThreadState(ip, port,
				serviceName), spanCollector, traceId));
	}
	

	/**
	 * 本地方法调用时，不需要Client send和Client receive， 只使用Server receive和Service send就可以了
	 * 
	 * @param spanName
	 */
	public static void newSpanWithServerRecvAnnotation(String spanName) {
		Hunter.newSpan(spanName);
		Hunter.submitAnnotation(zipkinCoreConstants.SERVER_RECV);
	}


	public static void submitServerSendAnnotationAndCollect() {
		Hunter.submitAnnotation(zipkinCoreConstants.SERVER_SEND);
		Hunter.collect();
	}

	public static void newSpanWithClientSendAnnotation(String spanName) {
		Hunter.newSpan(spanName);
		Hunter.submitAnnotation(zipkinCoreConstants.CLIENT_SEND);
	}

	public static void submitClientRecvAnnotationAndCollect() {
		Hunter.submitAnnotation(zipkinCoreConstants.CLIENT_RECV);
		Hunter.collect();
	}

	protected static void newSpan(String spanName) {
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
	protected static void submitAnnotation(String annotationName) {
		try {
			Hunter.TRACER.get().submitAnnotation(annotationName);
		} catch (Exception e) {
			LOG.warn("Submit annotation with name: " + annotationName
					+ " exception", e);
		}
	}

	/**
	 * 利用spanController发送trace log
	 */
	protected static void collect() {
		try {
			Hunter.TRACER.get().collect();
		} catch (Exception e) {
			LOG.warn("Collect trace log eception: ", e);
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
			// 这里不能使用Logger打Log，会引起死循环
		}
		return id;
	}

}
