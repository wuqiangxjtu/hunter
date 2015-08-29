package com.sina.amp.trace.hunter.struts2.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sina.amp.trace.hunter.struts2.service.ServiceA;
import com.sina.amp.trace.hunter.struts2.utils.AbsJsonObject;
import com.sina.amp.trace.hunter.struts2.utils.StatusCode;



public class HelloController {
	Logger LOG = LoggerFactory.getLogger(HelloController.class);
	
	@Autowired
	ServiceA serviceA;
	

	public ServiceA getServiceA() {
		return serviceA;
	}

	public void setServiceA(ServiceA serviceA) {
		this.serviceA = serviceA;
	}
	private AbsJsonObject jsonObject = new AbsJsonObject();

	public String hello() {
		LOG.info("hello begin");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serviceA.method1();
		jsonObject.setStatus(StatusCode.OPERATE_SUCCESS);
		jsonObject.addData("hello", "wuqiang");
		LOG.info("hello end");
		return "json";
	}
	
	

	public String happy() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		serviceA.method2();
		jsonObject.setStatus(StatusCode.OPERATE_SUCCESS);
		jsonObject.addData("happy", "wuqiang");
		return "json";
	}

	public AbsJsonObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(AbsJsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	


}
