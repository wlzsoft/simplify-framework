package vip.simplify.demo.system.util;

import java.io.Serializable;
import java.util.HashMap;

import vip.simplify.dao.util.DBUtil;
import vip.simplify.entity.IdEntity;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.util.HttpRequestPool;
import vip.simplify.utils.ReflectionUtil;

/**
 * <p><b>Title:</b><i>暂未使用</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月16日 下午5:39:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月16日 下午5:39:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
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
		    @SuppressWarnings("unchecked")
			Integer fid = ((IdEntity<Serializable,Integer>)curUserObj).getFid();
			if(t.getFid()==null) {
				params.put("createTime", DBUtil.getDbDate());
				if(curUserObj != null) {
					
					params.put("createId",  fid);
				}
			}
			params.put("updateTime", DBUtil.getDbDate());
			if(curUserObj != null) {
				params.put("updateId",  fid);
			}
//			ReflectionUtil.setProperties(t, params);
		}
}
