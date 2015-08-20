package com.sina.amp.trace.hunter;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;


public class HunterPatternLayout extends PatternLayout {

	public HunterPatternLayout(String pattern) {
		super(pattern);
	}

	public HunterPatternLayout() {
		super();
	}

	/**
	 * 重写createPatternParser方法，返回PatternParser的子类
	 */
	@Override
	protected PatternParser createPatternParser(String pattern) {
		return new HunterPatternParser(pattern);
	}
}