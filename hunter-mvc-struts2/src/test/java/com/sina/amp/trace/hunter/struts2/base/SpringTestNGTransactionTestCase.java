package com.sina.amp.trace.hunter.struts2.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

@ContextConfiguration(locations = { "/test-applicationContext.xml"})
public class SpringTestNGTransactionTestCase extends AbstractTransactionalTestNGSpringContextTests{
	@BeforeClass
	public void init() {
		
	}
}
