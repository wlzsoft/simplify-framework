package com.meizu.simplify.utils.serial;

/**
 * <p><b>Title:</b><i>序列化接口</i></p>
 * <p>Desc: 注意：必须提供唯一的serialVersionUID值，实现序列化接口
 * Fst , kryo , hessian , Protostuff ,Protostuff-Runtime, java等各种序列化方案，目前建议使用Fst序列化
 *          衡量序列化性能的标准有两个：1.序列化消耗的时间。2.序列化后对象占用的空间
 *          衡量序列化框架的可靠性的标准是： 需要在反序列化时做完整性校验，或是完整性校验交由通讯协议去保证</p>
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
public interface ISerialize {

	/**
	 * 
	 * 方法用途: 序列化<br>
	 * 操作步骤: 对象转数组<br>
	 * @param obj
	 * @return
	 */
	byte[] serialize(Object obj);

	/**
	 * 
	 * 方法用途: 反序列化<br>
	 * 操作步骤: 数组转对象<br>
	 * @param sec
	 * @return
	 */
	<D> D unserialize(byte[] sec);
}