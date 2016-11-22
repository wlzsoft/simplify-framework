package com.meizu.simplify.log.logback;

import com.meizu.demo.mvc.service.TestService;
import com.meizu.simplify.ioc.BeanFactory;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class RpcAppender extends AppenderBase<ILoggingEvent> {

  private PreSerializationTransformer<ILoggingEvent> pst = new LoggingEventPreSerializationTransformer();
	
  @Override
  public void start() {
    super.start();
  }

  @Override
  public void append(ILoggingEvent event) {
	  TestService testService = BeanFactory.getBean(TestService.class);
	  if(testService != null) {
		  LoggingEventVO so = (LoggingEventVO) pst.transform(event);
		  System.out.println(so.getFormattedMessage());
	  }
  }

}