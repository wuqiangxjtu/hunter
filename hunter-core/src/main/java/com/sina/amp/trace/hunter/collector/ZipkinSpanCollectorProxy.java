package com.sina.amp.trace.hunter.collector;

import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.twitter.zipkin.gen.Span;

/**
 * ZipkinSpanCollector的代理类
 * 原类在连不上zipkin collector的情况下会报错，导致项目无法运行
 * @author wuqiang
 *
 */
public class ZipkinSpanCollectorProxy implements SpanCollector{
	
	private ZipkinSpanCollector collector;
	
	public ZipkinSpanCollectorProxy(final String zipkinCollectorHost, final int zipkinCollectorPort) {
		try {
			this.collector = new ZipkinSpanCollector(zipkinCollectorHost,zipkinCollectorPort);
		} catch (Exception e) {
			e.printStackTrace();
			this.collector = null;
		}
	}

	@Override
	public void collect(Span span) {
		if (this.collector != null) {
			this.collector.collect(span);
		}
		
	}

	@Override
	public void addDefaultAnnotation(String key, String value) {
		if (this.collector != null) {
			this.collector.addDefaultAnnotation(key, value);
		}
	}

	@Override
	public void close() {
		if (this.collector != null) {
			this.collector.close();
		}
	}

}
