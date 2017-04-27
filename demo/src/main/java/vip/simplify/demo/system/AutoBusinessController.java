package vip.simplify.demo.system;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.demo.mvc.entity.Test;
import vip.simplify.dao.orm.BaseDao;
import vip.simplify.entity.IdEntity;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.annotation.RequestMap;
import vip.simplify.mvc.annotation.RequestParam;
import vip.simplify.mvc.model.Model;


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
public class AutoBusinessController extends SystemController<Model> {

	@RequestMap(path = {"/(.+)/get/(.+)$"})
	public List<?> get(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business, @RequestParam(defaultValue = "0", index = 2) String data)  {
		IAutoBusinessService service = BeanFactory.getBean(business+"AutoBusinessService");
		if(service==null) {
			return BaseDao.getIns(business).findByIds(data.split(","));
		}
		return service.get(data.split(","));
	}
	
	@RequestMap(path = {"/(.+)/del/(.+)$"})
	public int del(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business, @RequestParam(defaultValue = "0", index = 2) String data)  {
		IAutoBusinessService service = BeanFactory.getBean(business+"AutoBusinessService");
		if(service==null) {
			return BaseDao.getIns(business).remove(data.split(","));
		}
		return service.del(data.split(","));
	}
	
	@RequestMap(path = {"/(.+)/update/(.+)$"})
	public int update(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business, @RequestParam(defaultValue = "0", index = 2) String data)  {
		IAutoBusinessService service = BeanFactory.getBean(business+"AutoBusinessService");
		if(service==null) {
			Test test = new Test();
			test.setFid(1);
			test.setName("lcy-auto");
			return BaseDao.getIns(business).update(test);
		}
		return service.update(null,null);
	}
	
	@RequestMap(path = {"/(.+)/save$"})
	public boolean save(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(defaultValue = "", index = 1) String business)  {
		IAutoBusinessService service = BeanFactory.getBean(business+"AutoBusinessService");
		if(service==null) {
			Test test = new Test();
			test.setName("lcy-auto");
			IdEntity<Serializable, Integer> entity = test;
			boolean isSave = BaseDao.getIns(business).save(entity);
			return isSave;
		}
		return service.save(null,null);
		
	}
}