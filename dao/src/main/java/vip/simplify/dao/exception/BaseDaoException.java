package vip.simplify.dao.exception;
/**
  * <p><b>Title:</b><i>sql数据库操作异常</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午9:01:27</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午9:01:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BaseDaoException extends DataAccessException {

	private static final long serialVersionUID = -8493853380933833778L;
	public BaseDaoException(int errorCode, String message, Throwable target) {
		super(errorCode, message, target);
	}
	public BaseDaoException(int errorCode, String message) {
		super(errorCode, message);
	}
	public BaseDaoException(String message, Throwable target) {
		super(message, target);
	}
	public BaseDaoException(String message) {
		super(message);
	}
	public BaseDaoException(Throwable target) {
		super(target);
	}
	public BaseDaoException(int code) {
		super(code);
	}
	/**
	 * sql执行异常处理
	 * @param message 错误异常信息
	 * @param sql sql语句
	 */
	public BaseDaoException(String message,String sql) {
		super("执行sql异常:["+sql+"]"+message);
	}


}
