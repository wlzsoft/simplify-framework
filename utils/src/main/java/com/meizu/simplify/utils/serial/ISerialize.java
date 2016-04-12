package com.meizu.simplify.utils.serial;

/**
 * <p><b>Title:</b><i>序列化接口</i></p>
 * <p>Desc: 注意：必须提供唯一的serialVersionUID值，实现序列化接口</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午8:17:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午8:17:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface ISerialize<T extends Object> {

	/**
	 * 
	 * 方法用途: 序列化<br>
	 * 操作步骤: 对象转数组<br>
	 * @param obj
	 * @return
	 */
	byte[] serialize(T obj);

	/**
	 * 
	 * 方法用途: 反序列化<br>
	 * 操作步骤: 数组转对象<br>
	 * @param sec
	 * @return
	 */
	T unserialize(byte[] sec);
}