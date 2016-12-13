package com.meizu.simplify.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.api.service.IConfigService;
import com.meizu.simplify.config.system.SystemController;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.model.Model;


/**
 * <p><b>Title:</b><i>配置管理</i></p>
 * <p>Desc: 用于管理配置，使用角色为  [运维 ，开发，测试]</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午6:17:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午6:17:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@RequestMap(path = "/config")
public class ConfigController extends SystemController<Model> {
	
	@Resource
	private IConfigService configService;
	
	@RequestMap(path = "/get/")
	public String get(HttpServletRequest request, HttpServletResponse response, Model model)  {
		configService.get(null, "");
		return "jsp:/index";
	}
	
}