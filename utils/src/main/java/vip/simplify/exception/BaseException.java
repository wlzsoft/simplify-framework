package vip.simplify.exception;

/**
 * <p><b>Title:</b><i>异常基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:21:39</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:21:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class BaseException extends RuntimeException{
	private static final long serialVersionUID = 877984986571408117L;

	/**
	 * 状态码
	 */
	private int httpCode; 
	
	/**
	 * 目标异常
	 */
	private Throwable target;
	
	/**
	 * 构造一个基本异常
	 * @param message 异常信息
	 */
	public BaseException(String message) {
		super(message);
	}
	
	/**
	 * @param target 异常对象
	 */
	public BaseException(Throwable target) {
		super(target);
		this.target = target;
	}

	/**
	 * @param httpCode 状态码：如果是http，那么最终是http状态吗
	 */
	public BaseException(int httpCode) {
		this.setHttpCode(httpCode);
	}
	
	/**
	 * 构造一个基本异常
	 * @param httpCode 错误编码
	 * @param message 异常信息
	 */
	public BaseException(int httpCode,String message) {
		super(message);
		this.setHttpCode(httpCode);
	}
	
	/**
	 * 构造一个基本异常
	 * @param httpCode 错误编码
	 * @param message 异常信息
	 * @param target target
	 */
	public BaseException(int httpCode,String message,Throwable target) {
		super(message, target);
		this.setHttpCode(httpCode);
	}
	
	/**
	 * 构造一个基本异常
	 * @param message 异常信息
	 * @param target 目标异常
	 */
	public BaseException(String message, Throwable target) {
		super(message, target);
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}
	
	/**
	 * 
	 * 方法用途: 获取目标异常<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Throwable getTargetException() {
        return target;
    }
	
	/*
	 * 构建更友好的异常信息
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * 
	 * 方法用途: 获取根级异常<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * 
	 * 方法用途: 判断是否包含某异常类型<br>
	 * 操作步骤: TODO<br>
	 * @param exType
	 * @return
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof BaseException) {
			return ((BaseException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

}
