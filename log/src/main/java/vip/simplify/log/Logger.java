package vip.simplify.log;

import org.slf4j.LoggerFactory;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.enums.BeanTypeEnum;

/**
  * <p><b>Title:</b><i>日志业务类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月15日 上午10:39:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月15日 上午10:39:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean(type=BeanTypeEnum.PROTOTYPE)
public class Logger {
	
	private org.slf4j.Logger logger;
	
	public Logger(Class<?> beanClass) {
		logger = LoggerFactory.getLogger(beanClass);
	}
	
	public void info(String msg) {
		if(logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
	
	public void debug(String msg) {
		if(logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}
	
	public void error(String msg) {
		if(logger.isErrorEnabled()) {
			logger.error(msg);
		}
	}
}
