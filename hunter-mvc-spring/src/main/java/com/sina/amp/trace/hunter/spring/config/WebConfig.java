package com.sina.amp.trace.hunter.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = "com.sina.ad", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
public class WebConfig extends WebMvcConfigurerAdapter {
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {		
//		registry.addInterceptor(new ServletHandlerInterceptor());
//	}

}