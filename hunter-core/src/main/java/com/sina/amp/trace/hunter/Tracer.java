package com.sina.amp.trace.hunter;

import java.util.EmptyStackException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.fastjson.JSON;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.TraceFilter;
import com.sina.amp.trace.hunter.utils.ObjectUtil;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.Span;

public class Tracer {

	private final static Random RANDOM_GENERATOR = new Random();

	private final SpanCollector spanCollector;

	private final ThreadState state;

	private final Long traceId;
	
	//这里实际上是parent Span id，由于zipkin定义中是parent id，所以沿用了这个名称
	private final Long parentId;

	// 是否采样
	private AtomicBoolean IS_SAMPLE = new AtomicBoolean(true);

	public static Tracer getTracer(ThreadState state,
			SpanCollector spanCollector, Long traceId) {
		ObjectUtil.notNull(state, spanCollector, traceId);
		return new Tracer(state, spanCollector, null, traceId, null, null);
	}

	public static Tracer getTracer(ThreadState state,
			SpanCollector spanCollector, TraceFilters traceFilters, Long traceId) {
		ObjectUtil.notNull(state, spanCollector, traceFilters, traceId);
		return new Tracer(state, spanCollector, traceFilters, traceId, null,
				null);
	}
	
	public static Tracer getTracer(ThreadState state,
			SpanCollector spanCollector, Long traceId, Long parentId,
			Boolean isSample) {
		ObjectUtil.notNull(state, spanCollector, traceId,
				parentId, isSample);
		return new Tracer(state, spanCollector, null, traceId,
				parentId, isSample);
	}

	public static Tracer getTracer(ThreadState state,
			SpanCollector spanCollector, TraceFilters traceFilters,
			Long traceId, Long parentId, Boolean isSample) {
		ObjectUtil.notNull(state, spanCollector, traceFilters, traceId,
				parentId, isSample);
		return new Tracer(state, spanCollector, traceFilters, traceId,
				parentId, isSample);
	}



	private Tracer(ThreadState state, SpanCollector spanCollector,
			TraceFilters traceFilters, Long traceId, Long parentId,
			Boolean isSample) {
		this.state = state;
		this.spanCollector = spanCollector;
		this.traceId = traceId;
		this.parentId = parentId;

		if(hasParent(parentId)) {
			this.IS_SAMPLE.set(isSample);
		}else if(!hasParent(parentId) && traceFilters != null) {
			setIsSampleWithFilters(state, traceFilters);
		}
	}

	private void setIsSampleWithFilters(ThreadState state,
			TraceFilters traceFilters) {
		for (TraceFilter traceFilter : traceFilters.getTraceFilters()) {
			if (!traceFilter.trace(state.getEndpoint().getService_name())) {
				IS_SAMPLE.set(false);
			}
		}
	}

	private boolean hasParent(Long parentId) {
		return parentId != null;
	}

	public void collect() {
		if (IS_SAMPLE.get()) {
			Span span = state.pop();
			// System.out.println("--------------->>>>>"+JSON.toJSONString(span));;
			this.spanCollector.collect(span);
		}

	}

	/**
	 * 获取新的Span
	 * 
	 * @param spanName
	 * @return
	 */
	public void newSpan(String spanName) {
		if (IS_SAMPLE.get()) {
			try {
				Span currentSpan = null;
				currentSpan = state.peek();
				long newSpanId = RANDOM_GENERATOR.nextLong();
				Span newSpan = new Span(this.traceId, spanName, newSpanId,
						null, null);
				newSpan.setParent_id(currentSpan.getId());
				state.push(newSpan);

			} catch (EmptyStackException e) {
				Span newSpan = new Span(this.traceId, spanName, this.traceId,
						null, null);
				if (this.parentId != null) {
					newSpan.setParent_id(this.parentId);
				}
				state.push(newSpan);
			}

		}
	}

	public void submitAnnotation(String annotationName, long startTime,
			long endTime) {
		if (IS_SAMPLE.get()) {
			try {
				Span span = state.peek();
				if (span != null) {
					Annotation annotation = new Annotation();
					int duration = (int) (endTime - startTime);
					annotation.setTimestamp(startTime * 1000);
					annotation.setHost(state.getEndpoint());
					annotation.setDuration(duration * 1000);
					// Duration is currently not supported in the ZipkinUI, so
					// also
					// add
					// it as part of the annotation name.
					annotation.setValue(annotationName + "=" + duration + "ms");
					addAnnotation(span, annotation);
				}
			} catch (EmptyStackException e) {
				e.printStackTrace();
			}
		}
	}

	public void submitAnnotation(String annotationName) {
		if (IS_SAMPLE.get()) {
			try {
				Span span = state.peek();
				if (span != null) {
					Annotation annotation = new Annotation();
					annotation.setTimestamp(currentTimeMicroseconds());
					annotation.setHost(state.getEndpoint());
					annotation.setValue(annotationName);
					addAnnotation(span, annotation);
				}
			} catch (EmptyStackException e) {
				e.printStackTrace();
			}
		}
	}

	long currentTimeMicroseconds() {
		return System.currentTimeMillis() * 1000;
	}

	private void addAnnotation(Span span, Annotation annotation) {
		synchronized (span) {
			span.addToAnnotations(annotation);
		}
	}

	protected Long getTheTraceId() {
		return traceId;
	}

	protected AtomicBoolean getIS_SAMPLE() {
		return IS_SAMPLE;
	}

	protected SpanCollector getSpanCollector() {
		return spanCollector;
	}

	protected ThreadState getState() {
		return state;
	}

	protected Long getTraceId() {
		return traceId;
	}

	protected Long getParentId() {
		return parentId;
	}

}
