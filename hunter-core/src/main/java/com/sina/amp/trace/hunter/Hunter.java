package com.sina.amp.trace.hunter;


import javax.servlet.http.HttpServletRequest;
import com.github.kristofa.brave.SpanCollector;


public class Hunter {

	public static ThreadLocal<Tracer> TRACER = new ThreadLocal<Tracer>();
	
	/**
	 * Trace初始化
	 * @param request
	 * @param spanCollector
	 */
	public static void beginTrace(final HttpServletRequest request, final SpanCollector spanCollector, final TraceFilters traceFilters) {
		beginTrace(request.getLocalAddr(), request.getLocalPort(), getServiceName(request), spanCollector, traceFilters);
	}
	
	public static void beginTrace(String ip, int port, String serviceName,SpanCollector spanCollector, final TraceFilters traceFilters) {
		Hunter.TRACER.set(new Tracer(new ThreadState(ip,
				port, serviceName), spanCollector,traceFilters));
	}
	
	/**
	 * 新建一个span
	 * @param request
	 */
	public static void newSpan(final HttpServletRequest request) {
		newSpan(getSpanName(null, request));
	}
	
	public static void newSpan(String serviceName) {
		Hunter.TRACER.get().newSpan(serviceName);
	}
	
	/**
	 * 给当前span添加annotation
	 * @param annotationName
	 */
	public static void submitAnnotation(String annotationName) {
		Hunter.TRACER.get().submitAnnotation(annotationName);
	}
	
	/**
	 * 利用spanController发送trace log
	 */
	public static void collect() {
		Hunter.TRACER.get().collect();
	}
	
	public static void clear() {
		Hunter.TRACER.remove();
	}

	private static String getSpanName(final String name,
			final HttpServletRequest request) {
		if (name == null || name.trim().isEmpty()) {
			return request.getRequestURI();
		}
		return name;
	}
	
	private static String getServiceName(final HttpServletRequest request) {
		return getSpanName(null, request);
	}

}
