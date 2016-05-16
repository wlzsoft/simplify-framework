package com.meizu.simplify.cache.redis.util;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.JsonUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月16日 下午6:21:38</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月16日 下午6:21:38</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class JsonResolver {
	@Resource
	private SerializeFilter afterFilter;
	public String ObjectToString(Object obj ) {
		if(afterFilter == null) {
			return JsonUtil.objectToStringAndContainMeta(obj);
		} else {
			return JsonUtil.objectToString(obj, afterFilter);
		}
	}
}
