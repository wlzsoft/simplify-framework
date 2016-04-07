package com.meizu.simplify.ioc;
/**
  * <p><b>Title:</b><i>bean实体</i></p>
 * <p>Desc: 包含bean相关的信息，除了bean对象本身，还可以有bean的别名，类型等meta元数据信息</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月29日 上午10:26:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月29日 上午10:26:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeanEntity<T extends Object> {
	
	private String name;
	
	private T beanObj;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public T getBeanObj() {
		return beanObj;
	}
	public void setBeanObj(T beanObj) {
		this.beanObj = beanObj;
	}
	
}
