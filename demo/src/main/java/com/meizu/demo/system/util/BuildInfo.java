package com.meizu.demo.system.util;

import java.io.Serializable;
import java.util.HashMap;

import com.meizu.simplify.dao.util.DBUtil;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.util.HttpRequestPool;
import com.meizu.simplify.utils.ReflectionUtil;

/**
 * <p><b>Title:</b><i>暂未使用</i></p>
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

	public void buildId(T t) {
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("delFlag", true);
			Object curUserObj = null;
		    try {
		    	Object request = HttpRequestPool.getRequest();
		    	Object bean = BeanFactory.getBean("systemService");
		    	curUserObj = ReflectionUtil.invokeMethod(bean, "getCurrentUser", new Class[]{Object.class}, new Object[]{request});
		    } catch(Exception e) {
		    	//e.printStackTrace();
		    }
			if(t.getFid()==null) {
				params.put("createTime", DBUtil.getDbDate());
				if(curUserObj != null) {
					params.put("createId",  ((IdEntity<Serializable,Integer>)curUserObj).getFid());
				}
			}
			params.put("updateTime", DBUtil.getDbDate());
			if(curUserObj != null) {
				params.put("updateId",  ((IdEntity<Serializable,Integer>)curUserObj).getFid());
			}
//			ReflectionUtil.setProperties(t, params);
		}
}
