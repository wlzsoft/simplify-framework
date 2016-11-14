package com.meizu.simplify.log.hook;
/**
  * <p><b>Title:</b><i>Log多例工厂钩子函数</i></p>
 * <p>Desc: 用于生成Log实例，以是否标注@Resource来注入实例，而并非是标注Bean注解
 *          注意：比较特殊，和rpc的client实例有点类似,并非自己可以编写的类实例，无法通过Bean注解来解析
 *          TODO 后续考虑第三方库的实例如果在不能@Bean的方法是初始化，是否通过配置方式</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.annotation.BeanPrototypeHook;
import com.meizu.simplify.ioc.hook.IBeanPrototypeHook;

@BeanPrototypeHook(Logger.class)
public class LogPrototypeHook implements IBeanPrototypeHook<Logger> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogPrototypeHook.class);

	@Override
	public List<BeanEntity<Logger>> hook(Class<Logger> clazz) {
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("开始初始化SLFJ的Log实例....");
		}
		
		return null;
	}
}
