package com.meizu.simplify.mvc.model;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.ioc.annotation.DefaultBean;

/**
  * <p><b>Title:</b><i>controller表单对象选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:37:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:37:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface IModelSelector {

	/**
	 * 方法用途: controller表单对象选择处理-设置表单数据<br>
	 * 操作步骤: 目前限制只能注入Model类型的实体<br>
	 * @param request
	 * @param modelClass
	 * @param cmd
	 * @param urlparams
	 * @return
	 */
	public <T> T setRequestModel(HttpServletRequest request, Class<T> modelClass, String cmd, String[] urlparams);
}
