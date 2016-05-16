package com.meizu.simplify.template;

import java.io.IOException;
import java.util.Map;

import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.template.annotation.TemplateExtend;
import com.meizu.simplify.template.annotation.TemplateType;
import com.meizu.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>模版处理回调接口</i></p>
 * <p>Desc: 注意：后续所有的模版引擎，都需要通过独立成模块，以插件形式接入到框架中</p>
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
	/**
	 * 方法用途: 执行模版渲染，并获取模版渲染后的静态内容<br>
	 * 操作步骤: TODO<br>
	 * @param parameters 业务参数值
	 * @param templateUrl 模版地址
	 * @param prefixUri 模版地址前缀
	 * @param extend 模版扩展名
	 * @return 解析后的模版
	 * @throws IOException
	 */
	public default String render(Map<String, Object> parameters,String templateUrl, String prefixUri,String extend) throws IOException {
		return null;
	}
	/**
	 * 方法用途: 执行模版渲染，并获取模版渲染后的静态内容<br>
	 * 操作步骤: TODO<br>
	 * @param parameters 业务参数值
	 * @param templateUrl 模版地址
	 * @param prefixUri 模版地址前缀
	 * @return 解析后的模版
	 * @throws IOException
	 */
	public default String render(Map<String, Object> parameters,String templateUrl, String prefixUri) throws IOException {
		return null;
	}
	/**
	 * 方法用途: 获取模版文件扩展名<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public default String getExtend() {
		TemplateExtend templateExtend = this.getClass().getAnnotation(TemplateExtend.class);
		if(templateExtend == null) {
			return null;
		}
		String extend = templateExtend.extend();
		if(StringUtil.isNotBlank(extend)) {
			extend = "."+extend;
		}
		return extend;
	}
	
}
