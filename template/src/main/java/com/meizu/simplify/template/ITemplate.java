package com.meizu.simplify.template;

import java.io.IOException;
import java.util.Map;

import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.template.annotation.TemplateType;
import com.meizu.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>模版处理回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean(BeetlTemplate.class)
public interface ITemplate {
	public default String render(Map<String, Object> parameters,String templateUrl, String prefixUri) throws IOException {
		return null;
	}
	public default String getExtend() {
		TemplateType templateType = this.getClass().getAnnotation(TemplateType.class);
		if(templateType == null) {
			return null;
		}
		String extend = templateType.extend();
		if(StringUtil.isNotBlank(extend)) {
			extend = "."+extend;
		}
		return extend;
	}
	
}
