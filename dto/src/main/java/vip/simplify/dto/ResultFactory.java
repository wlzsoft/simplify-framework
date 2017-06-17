package vip.simplify.dto;

/**
 * 
 * <p><b>Title:</b><i>结果信息构建工厂</i></p>
 * <p>Desc: 提供各种预制结果消息体</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年1月14日 上午10:39:26</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年1月14日 上午10:39:26</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class ResultFactory {
	
	private static Result forbiddenResult = null;//可以设置有效期
	/**
	 * 方法用途: 未授权提示<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result forbidden() {
		if(forbiddenResult != null) {
			return forbiddenResult;
		}
		forbiddenResult = new ResultObject<String>("您没有执行该操作的权限，请与管理员联系。");
		forbiddenResult.setCode(HttpStatusEnum.FORBIDDEN.value());
		return forbiddenResult;
	}
	
	private static Result failResult = null;//可以设置有效期
	/**
	 * 方法用途: 处理失败-未处理异常结果体<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result fail() {
		if(failResult != null) {
			return failResult;
		}
		failResult = new ResultObject<String>("服务器暂时繁忙，请稍候重试或与管理员联系。");
		failResult.setCode(HttpStatusEnum.INTERNAL_SERVER_ERROR.value());
		return failResult;
	}
	
	private static Result timeoutResult;//可以设置有效期
	/**
	 * 方法用途: 超时提示<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result timeout() {
		if(timeoutResult != null) {
			return timeoutResult;
		}
		timeoutResult = new ResultObject<String>("服务已经超时。");
		timeoutResult.setCode(HttpStatusEnum.TIME_OUT.value());
		return timeoutResult;
	}
	
	private static Result loginResult = null;//可以设置有效期
	/**
	 * 方法用途: 登陆提示<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result login() {
		if(loginResult != null) {
			return loginResult;
		}
		loginResult = new ResultObject<String>("请登录。");
		loginResult.setCode(HttpStatusEnum.UNAUTHORIZED.value());
		return loginResult;
	}
	
	/**
	 * 方法用途: 500错误：自定义错误提示信息,服务器端异常，非业务异常<br>
	 * 操作步骤: TODO<br>
	 * @param message
	 * @return
	 */
	public static Result error(Object message) {//TODO map存储结果，可设置有效期，定期从map中移除
		Result result = new ResultObject<Object>(message);
		result.setCode(HttpStatusEnum.INTERNAL_SERVER_ERROR.value());
		return result;
	}
	
	/**
	 * 方法用途: 自定义成功提示<br>
	 * 操作步骤: TODO<br>
	 * @param message
	 * @return
	 */
	public static Result success(Object message) {//TODO map存储结果，可设置有效期，定期从map中移除
		Result result = new ResultObject<Object>(message);
		result.setCode(HttpStatusEnum.OK.value());
		result.setSuccess(true);
		return result;
	}

}