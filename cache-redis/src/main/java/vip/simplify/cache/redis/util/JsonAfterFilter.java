package vip.simplify.cache.redis.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.AfterFilter;
import vip.simplify.cache.CacheProxyDao;
import vip.simplify.cache.ICacheDao;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.utils.ObjectUtil;

/**
 * TODO 反射待优化
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月9日 下午5:46:06</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年3月9日 下午5:46:06</p>
 * @author <a href="wanghaibin@meizu.com" >meizu</a>
 * @version Version 1.0
 *
 */
@Bean
public class JsonAfterFilter extends AfterFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(AfterFilter.class);

	@Override
	public void writeAfter(Object object) {
		Field[] files=object.getClass().getSuperclass().getDeclaredFields();
		for(Field file:files){
			if(file.getName().equals("createId") ){
				try {
					if(ObjectUtil.isNotNull(file.get(object)) && file.get(object).toString()!="0"){
						Integer createId =Integer.parseInt(file.get(object).toString());
						ICacheDao<String, Object> cachedDao = CacheProxyDao.getCache();
						Object user=cachedDao.get("curUser"+createId);
						if(null!=user){
							JSONObject objct=JSONObject.parseObject(user.toString());
							this.writeKeyValue("createName", objct.get("fuserName"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			if(file.getName().equals("updateId") ){
				try {
					if(ObjectUtil.isNotNull(file.get(object)) && file.get(object).toString()!="0"){
						Integer updateId =Integer.parseInt(file.get(object).toString());
						ICacheDao<String, Object> cachedDao = CacheProxyDao.getCache();
						Object user=cachedDao.get("curUser"+updateId);
						if(null!=user){
							JSONObject objct=JSONObject.parseObject(user.toString());
							this.writeKeyValue("updateName", objct.get("fuserName"));
						}
					}
				} catch (Exception e) {
					logger.error("查询用户名称异常！"+e);
				} 
			}
		}
	}

}
