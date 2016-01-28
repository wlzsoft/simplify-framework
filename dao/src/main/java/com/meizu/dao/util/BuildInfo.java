package com.meizu.dao.util;

import java.io.Serializable;
import java.util.HashMap;

import com.meizu.dao.exception.HttpServletRequest;
import com.meizu.entity.IdEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月16日 下午5:39:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月16日 下午5:39:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Bean
public class BuildInfo<T extends IdEntity<Serializable,Integer>> {
//	@Resource
//	private HttpServletRequest request;
	public void buildId(T t) {
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("delFlag", true);
			Object curUserObj = null;
		    try {
		    	HttpServletRequest request = null;//request池中获取request对象，而无需传参的方式 
		    	Object bean = BeanFactory.getBean("systemService");//.getCurrentUser(request);
//		    	curUserObj = ReflectionUtil.invokeMethod(bean, "getCurrentUser", null, new Class[]{HttpServletRequest.class}, new Object[]{request});
		    	//curUserObj = request.getSession().getAttribute("curUser"); //TODO
		    } catch(Exception e) {
		    	//e.printStackTrace();
		    	
		    }
			if(t.getId()==null) {
//				params.put("createTime", DBUtil.getMysqlDbDate());
				if(curUserObj != null) {
					params.put("createId",  ((IdEntity<Serializable,Integer>)curUserObj).getId());
				}
			}
//			params.put("updateTime", DBUtil.getMysqlDbDate());
			if(curUserObj != null) {
				params.put("updateId",  ((IdEntity<Serializable,Integer>)curUserObj).getId());
			}
//			ReflectionUtil.setProperties(t, params);
		}
}
