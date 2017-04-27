package vip.simplify.util;

import com.alibaba.fastjson.serializer.SerializeFilter;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Resource;
import vip.simplify.utils.JsonUtil;

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
			return JsonUtil.objectToString(obj);
		} else {
			return JsonUtil.objectToString(obj, afterFilter);
		}
	}
}
