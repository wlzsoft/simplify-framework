package com.meizu.simplify.cache;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.annotation.BeanPrototypeHook;
import com.meizu.simplify.ioc.hook.IBeanPrototypeHook;
/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
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
@BeanPrototypeHook(CacheInterceptor.class)
public class CacheInterceptorPrototypeHook implements IBeanPrototypeHook<CacheInterceptor> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheInterceptorPrototypeHook.class);
	
	@Override
	public List<BeanEntity<CacheInterceptor>> hook(Class<CacheInterceptor> clazz) {
		
		LOGGER.debug("开始初始化CacheInterceptor实例....");
		List<BeanEntity<CacheInterceptor>> list = new ArrayList<>();
		
		//init beforeCacheInterceptor
		String beanName = "beforeCacheInterceptor";
		BeanEntity<CacheInterceptor> beanEntity = new BeanEntity<>();
		beanEntity.setName(beanName);
		beanEntity.setBeanObj(CacheInterceptor.getBeforeInstance());
		list.add(beanEntity);
		LOGGER.info("已注入bean:CacheInterceptor[{}]", beanName);
		//init afterCacheInterceptor
		beanName = "afterCacheInterceptor";
		beanEntity = new BeanEntity<>();
		beanEntity.setName(beanName);
		beanEntity.setBeanObj(CacheInterceptor.getAfterInstance());
		list.add(beanEntity);
		LOGGER.info("已注入bean:CacheInterceptor[{}]", beanName);
		
		return list;
	}

	

}
