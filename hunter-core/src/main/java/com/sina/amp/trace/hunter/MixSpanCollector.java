package com.sina.amp.trace.hunter;

import com.github.kristofa.brave.LoggingSpanCollector;
import com.github.kristofa.brave.SpanCollector;
import com.github.kristofa.brave.zipkin.ZipkinSpanCollector;
import com.twitter.zipkin.gen.Span;

public class MixSpanCollector implements SpanCollector{
	
	private SpanCollector loggingCollector;
	private SpanCollector zipkinConllector;
	
	public MixSpanCollector(String host, int port) {
		try {
			this.loggingCollector = new LoggingSpanCollector();
			this.zipkinConllector = new ZipkinSpanCollector(host, port);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void collect(Span span) {
		this.loggingCollector.collect(span);
		this.zipkinConllector.collect(span);
		
	}

	@Override
	public void addDefaultAnnotation(String key, String value) {
		loggingCollector.addDefaultAnnotation(key, value);
		zipkinConllector.addDefaultAnnotation(key, value);
		
	}

	@Override
	public void close() {
		loggingCollector.close();
		zipkinConllector.close();
	}
}
