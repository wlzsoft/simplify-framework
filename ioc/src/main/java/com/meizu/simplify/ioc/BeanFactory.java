package com.meizu.simplify.ioc;

import java.util.List;

/**
  * <p><b>Title:</b><i>对象工厂</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午12:39:03</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午12:39:03</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeanFactory {
	private static final BeanContainer beanContainer = new BeanContainer();

	public static BeanContainer getBeanContainer() {
		return beanContainer;
	}
	
	/**
	 * 
	 * 方法用途: 从容器中获取bean对象<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
	 * @return
	 */
	public static <T> T getBean(Class<T> beanClass) {
		String beanClassName = beanClass.getName();
		return getBean(beanClassName);
	}
	
	/**
	 * 
	 * 方法用途: 从容器中获取bean对象<br>
	 * 操作步骤: TODO<br>
	 * @param beanClassName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  static <T> T getBean(String beanClassName) {
		return (T) beanContainer.getMapContainer().get(beanClassName);
	}
	
	/**
	 * 
	 * 方法用途: 添加bean对象到容器中<br>
	 * 操作步骤: TODO<br>
	 * @param beanObj
	 */
	public static void addBean(Object beanObj) {
		beanContainer.add(beanObj);
	}
	/**
	 * 
	 * 方法用途: 添加多个bean对象到容器中<br>
	 * 操作步骤: TODO<br>
	 * @param beanList
	 */
	public static void addBeanList(List<?> beanList) {
		for (Object object : beanList) {
			addBean(object);
		}
	}
}
