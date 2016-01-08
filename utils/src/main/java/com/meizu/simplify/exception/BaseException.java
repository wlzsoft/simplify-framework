package com.meizu.simplify.exception;

/**
 * <p><b>Title:</b><i>异常基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:21:39</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:21:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class BaseException extends RuntimeException{
	private static final long serialVersionUID = 877984986571408117L;

	/**
	 * 错误编码
	 */
	private  int errorCode; 
	
	/**
	 * 构造一个基本异常
	 * @param message 异常信息
	 */
	public BaseException(String message) {
		super(message);
	}
	
	/**
	 * @param cause 异常对象
	 */
	public BaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param code 状态码：如果是http，那么最终是http状态吗
	 */
	public BaseException(int code) {
		this.setErrorCode(code);
	}
	
	/**
	 * 构造一个基本异常
	 * @param errorCode 错误编码
	 * @param message 异常信息
	 */
	public BaseException(int errorCode,String message) {
		super(message);
		this.setErrorCode(errorCode);
	}
	
	/**
	 * 构造一个基本异常
	 * @param errorCode 错误编码
	 * @param message 异常信息
	 */
	public BaseException(int errorCode,String message,Throwable cause) {
		super(message, cause);
		this.setErrorCode(errorCode);
	}
	
	/**
	 * 构造一个基本异常
	 * @param message 异常信息
	 * @param cause 根异常类（可以存入任何异常）
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
