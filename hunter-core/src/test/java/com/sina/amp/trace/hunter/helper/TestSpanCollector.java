package com.sina.amp.trace.hunter.helper;

import java.util.ArrayList;

import com.github.kristofa.brave.SpanCollector;
import com.twitter.zipkin.gen.Span;

public class TestSpanCollector implements SpanCollector{
	
	private ArrayList<Span> spans = new ArrayList<Span>();
	

	public ArrayList<Span> getSpans() {
		return spans;
	}

	@Override
	public void collect(Span span) {
		spans.add(span);
	}

	@Override
	public void addDefaultAnnotation(String key, String value) {
		
	}

	@Override
	public void close() {
		
	}
	
	public void clear() {
		spans = new ArrayList<Span>();
	}

}
