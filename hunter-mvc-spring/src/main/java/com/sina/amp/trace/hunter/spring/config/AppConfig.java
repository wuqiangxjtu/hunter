package com.sina.amp.trace.hunter.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

//@Configuration
//@ComponentScan(basePackages = "com.sina.ad;com.github.kristofa.brave", excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
//@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AppConfig {
}