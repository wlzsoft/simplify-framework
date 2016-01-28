package com.meizu.dao.dto;


/**
 * 
 * <p><b>Title:</b><i>传输JSON对象</i></p>
 * <p>Desc: 传输JSON对象</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:56:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:56:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class Result implements IResult{
	
	/**
	 * 成功标志,注意： 最好success是一个object类型的，不仅仅可以是boolean的返回，有时可以定制，比如 “success” 这样的字符串才是表示成功返回。
	 */
	private boolean success;
	
	/**
	 * 状态码
	 */
	private String statusCode;
	
	/**
	 * 失败消息
	 */
	private String message;
	
	/**
	 * 时间撮
	 */
	private long dateline = System.currentTimeMillis();
	
	public Result() {
		
	}
	public Result(String message) {
		this.message = message;
	}

	
	


	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	public long getDateline() {
		return dateline;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
