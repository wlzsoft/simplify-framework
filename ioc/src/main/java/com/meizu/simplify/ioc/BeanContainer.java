package com.meizu.simplify.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午12:40:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午12:40:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeanContainer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanContainer.class);
	private final Map<String,Object> mapContainer = new ConcurrentHashMap<>();
	
	public Map<String, Object> getMapContainer() {
		return mapContainer;
	}

	/**
	 * 
	 * 方法用途: 保存创建的bean对象到容器中<br>
	 * 操作步骤: TODO<br>
	 * @param beanObj
	 */
	public <T extends Object> void add(T beanObj) {
		String beanName = beanObj.getClass().getName();
		add(beanName,beanObj);
	}
	
	/**
	 * 
	 * 方法用途: 保存创建的bean对象到容器中<br>
	 * 操作步骤: TODO<br>
	 * @param beanName
	 * @param beanObj
	 */
	public <T extends Object> void add(String beanName,T beanObj) {
		mapContainer.put(beanName, beanObj);
		LOGGER.debug("成功添加bean实例["+beanName+"]到容器中:"+beanObj);
	}
	
	
}
