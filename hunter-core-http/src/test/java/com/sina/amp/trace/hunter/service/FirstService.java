package com.sina.amp.trace.hunter.service;

import org.apache.log4j.Logger;
import com.sina.amp.trace.hunter.http.HttpHunter;

public class FirstService {

	Logger log = Logger.getLogger(FirstService.class);
	


	public void serviceA() {
		log.info("-----------serviceA-----------");
		HttpHunter.newSpanWithServerRecvAnnotation("serviceA");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		serviceB();
		HttpHunter.submitServerSendAnnotationAndCollect();
		

		
	}
	
	public void serviceB() {
		log.info("-----------serviceB-----------");
		HttpHunter.newSpanWithServerRecvAnnotation("serviceB");
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HttpHunter.submitServerSendAnnotationAndCollect();
	}
}
