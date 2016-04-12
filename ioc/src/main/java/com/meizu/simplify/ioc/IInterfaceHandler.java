package com.meizu.simplify.ioc;

/**
  * <p><b>Title:</b><i>bean多实现类选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 下午3:36:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 下午3:36:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IInterfaceHandler {
	/**
	 * 
	 * 方法用途: 返回被选择的实现类类型<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Class<?> handle();
}
