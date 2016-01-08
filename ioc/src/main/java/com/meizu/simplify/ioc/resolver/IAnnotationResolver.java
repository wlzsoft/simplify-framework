package com.meizu.simplify.ioc.resolver;

import java.util.List;

/**
  * <p><b>Title:</b><i>解析器</i></p>
 * <p>Desc: 解析各种注解信息</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:19:57</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:19:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 * @param <R> 指定解析数据的类型
 */
public interface IAnnotationResolver<R extends Object> {
	
	/**
	 * 
	 * 方法用途: 解析注解<br>
	 * 操作步骤: TODO<br>
	 * @param resolveList 解析的数据列表
	 */
	void resolve(List<R> resolveList);

}
