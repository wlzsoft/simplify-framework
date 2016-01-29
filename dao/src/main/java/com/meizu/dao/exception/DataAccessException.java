package com.meizu.dao.exception;

import com.meizu.simplify.exception.BaseException;

/**
  * <p><b>Title:</b><i>数据访问异常</i></p>
 * <p>Desc: 包含关系型数据库异常，缓存数据异常，本地缓存数据异常等等</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午3:24:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午3:24:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class DataAccessException extends BaseException{

	private static final long serialVersionUID = -5952392928142126725L;
	public DataAccessException(int code) {
		super(code);
	}
	public DataAccessException(int errorCode, String message, Throwable target) {
		super(errorCode, message, target);
	}
	public DataAccessException(int errorCode, String message) {
		super(errorCode, message);
	}
	public DataAccessException(String message, Throwable target) {
		super(message, target);
	}
	public DataAccessException(String message) {
		super(message);
	}
	public DataAccessException(Throwable target) {
		super(target);
	}


}
