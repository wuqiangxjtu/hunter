package com.sina.amp.trace.hunter.struts2.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(locations = {"/test-applicationContext.xml"})
public class SpringTestNGTestCase extends AbstractTestNGSpringContextTests{

}
