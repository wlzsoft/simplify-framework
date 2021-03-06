package vip.simplify.cache.redis.exception;

import vip.simplify.exception.BaseException;

/**
  * <p><b>Title:</b><i>redis异常</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午5:59:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午5:59:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisException extends BaseException {


	private static final long serialVersionUID = 7608271882621064719L;
	public RedisException(Exception e) {
		super(e);
	}
	public RedisException(String message) {
		super(message);
	}

	public RedisException(String message,Exception e) {
		super(message,e);
	}
}
