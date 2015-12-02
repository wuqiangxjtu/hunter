package com.sina.amp.trace.hunter.utils;

public class ObjectUtil {
	
	public static void notNull(Object ... objects) {
		for(Object object : objects) {
			if(object == null) throw new RuntimeException("object is null.");
		}
	}

}
