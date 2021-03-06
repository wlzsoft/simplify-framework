package vip.simplify.cache.redis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import vip.simplify.cache.redis.util.RedisHostAndPortUtil;
import vip.simplify.cache.redis.util.RedisHostAndPortUtil.HostAndPort;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午1:39:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午1:39:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

public class HostAndPortUtilTest {
	@Test
	public  void test() {
//		redis_ref_hosts=192.168.168.208:6379:foobared,192.168.1.218:6379:foobared,192.168.168.208:6379:foobared,www.demo.com:6379:foobared
		Map<String, List<HostAndPort>> map =RedisHostAndPortUtil.getRedisServers();
		Set<Entry<String, List<HostAndPort>>> set = map.entrySet();
		for (Entry<String, List<HostAndPort>> entry : set) {
			System.out.println(entry.getKey()+":[size:"+entry.getValue().size()+"]");
		}
	}
}
