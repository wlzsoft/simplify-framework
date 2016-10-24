package com.meizu.simplify.mvc.view;

import com.meizu.simplify.mvc.resolver.TemplateAnnotationResolver;
import com.meizu.simplify.view.IPageTemplate;

/**
  * <p><b>Title:</b><i>模版工厂</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 上午9:51:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 上午9:51:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class TemplateFactory {

	/**
	 * 
	 * 方法用途: 获取指定类型的模版<br>
	 * 操作步骤: TODO<br>
	 * @param templateType 模版类型
	 * @return
	 */
	public static IPageTemplate getTemplate(String templateType) {
		return	TemplateAnnotationResolver.templateMap.get(templateType);
	}

}
