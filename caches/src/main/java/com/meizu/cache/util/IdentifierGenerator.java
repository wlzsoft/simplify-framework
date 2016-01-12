package com.meizu.cache.util;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年8月6日 下午7:43:20</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年8月6日 下午7:43:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface IdentifierGenerator {
	public long incr(String key); 
	public long incrBy(String key, long value);
}
