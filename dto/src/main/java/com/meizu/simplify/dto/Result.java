package com.meizu.simplify.dto;


/**
 * 
 * <p><b>Title:</b><i>传递结果基本信息</i></p>
 * <p>Desc: 传递结果基本信息</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:56:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:56:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 */
public class Result {
	
	/**
	 * 成功标志,注意： 最好success是一个object类型的，不仅仅可以是boolean的返回，有时可以定制，比如 “success” 这样的字符串才是表示成功返回。
	 */
	private boolean success;
	
	/**
	 * 状态码
	 */
	private String statusCode;
	
	/**
	 * 时间撮
	 */
	private long dateline = System.currentTimeMillis();
	
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
	
	@Override
	public String toString() {
		return "Result [success=" + success + ", statusCode=" + statusCode + ", dateline="
				+ dateline + "]";
	}
}
