package com.meizu.simplify.dto;

/**
 * 
 * <p><b>Title:</b><i>json数据结果体</i></p>
 * <p>Desc: TODO</p>
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
public class JsonResult extends Result {
	
	public static Result forbidden() {
		Result result = new Result("您没有执行该操作的权限，请与管理员联系。");
		result.setStatusCode(HttpStatus.FORBIDDEN.toString()+"");
		return result;
	}
	/**
	 * 方法用途: 处理失败-未处理异常结果体<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result fail() {
		Result result = new Result("服务器暂时繁忙，请稍候重试或与管理员联系。");
		result.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()+"");
		return result;
	}
	/**
	 * 方法用途: 自定义错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param message
	 * @return
	 */
	public static Result error(Object message) {
		Result result = new Result(message);
		result.setStatusCode(HttpStatus.MULTIPLE_CHOICES.toString()+"");
		return result;
	}
	/**
	 * 方法用途: 超时提示<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result timeout() {
		Result model = new Result("会话超时，请重新登录。");
		model.setStatusCode(HttpStatus.MOVED_PERMANENTLY.toString()+"");
		return model;
	}
	/**
	 * 方法用途: 登陆提示<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Result login() {
		Result model = new Result("请登录。");
		model.setStatusCode(HttpStatus.MOVED_PERMANENTLY.toString()+"");
		return model;
	}
	/**
	 * 方法用途: 成功提示<br>
	 * 操作步骤: TODO<br>
	 * @param message
	 * @return
	 */
	public static Result success(Object message) {
		Result result = new Result(message);
		result.setStatusCode(HttpStatus.OK.value()+"");
		return result;
	}
}