package com.meizu.demo.system;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.demo.mvc.model.TestModel;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.model.Model;


/**
 * <p><b>Title:</b><i>自动controller处理类</i></p>
 * <p>Desc: 和业务代码生成模块集成，可以减少大量工作量
 *          可以定制一个Service层的接口，只需要编写</p>
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
public class AutoBusinessController extends SystemController<TestModel> {

	@RequestMap(path = {"/(.+)/get/(.+)$"})
	public IdEntity<Serializable, Integer> get(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business, @RequestParam(defaultValue = "0", index = 2) String data)  {
		IAutoBusinessService service = BeanFactory.getBean(business+"AutoBusinessService");
		if(service==null) {
			return BaseDao.getIns(business).findById(data);
		}
		return service.get(data);
	}
	
	@RequestMap(path = {"/(.+)/save$"})
	public boolean add(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business)  {
		Test test = new Test();
		test.setName("lcy-auto");
		IdEntity<Serializable, Integer> entity = test;
		boolean isSave = BaseDao.getIns(business).save(entity);
		return isSave;
	}
}