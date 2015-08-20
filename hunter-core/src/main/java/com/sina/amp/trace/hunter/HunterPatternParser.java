package com.sina.amp.trace.hunter;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class HunterPatternParser extends PatternParser {
	
	private static final char TOKEN = 'T';

	public HunterPatternParser(String pattern) {
		super(pattern);
	}

	/**
	 * 重写finalizeConverter，对特定的占位符进行处理，T表示线程ID占位符
	 */
	@Override
	protected void finalizeConverter(char c) {
		if (c == TOKEN) {
			this.addConverter(new ExPatternConverter(this.formattingInfo));
		} else {
			super.finalizeConverter(c);
		}
	}

	private static class ExPatternConverter extends PatternConverter {

		public ExPatternConverter(FormattingInfo fi) {
			super(fi);
		}

		/**
		 * 返回traceid
		 * 1. 从ThreadLocal中获取
		 * 2. 如果没有则返回NULL
		 */
		@Override
		protected String convert(LoggingEvent event) {
			String traceId = "NULL";
			if(Hunter.getTraceId() != null) {
				traceId = "[ Trace id: " + Hunter.getTraceId().toString() + " ]";
			}
			return traceId;
		}

	}
}