package com.meizu.simplify.dto;

/**
 * <p><b>Title:</b><i>自定义结果状态码枚举</i></p>
* <p>Desc: 注意：和HttpStatusEnum有部分状态码会对应起来，具体看枚举值的说明</p>
* <p>source folder:{@docRoot}</p>
* <p>Copyright:Copyright(c)2014</p>
* <p>Company:meizu</p>
* <p>Create Date:2016年1月27日 下午5:26:04</p>
* <p>Modified By:luchuangye-</p>
* <p>Modified Date:2016年1月27日 下午5:26:04</p>
* @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
* @version Version 0.1
*
*/
public enum ResultStatusEnum {
	/** 
	 * 成功  mapper http code 200
	 */
	OK(0),
	/** 
	 * 请求前端报错，还未正式处理业务：比如参数无效   mapper http code 400 
	 */
	INVALID(3),
	/**
	 * 未授权：没有权限访问  mapper http code 403
	 */
	FORBIDDEN(4),
	/**
	 * 服务器内部错误   mapper http code 500
	 */
	ERROR(1),
	/** 
	 * 服务器超时  mapper http code 503
	 */
	TIME_OUT(2);

	private int statusCode;
	ResultStatusEnum(int statusCode) {
		this.statusCode = statusCode;
	}
	public int value() {
		return statusCode;
	}
}
