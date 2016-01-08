package com.meizu.simplify.exception;

/**
 * <p><b>Title:</b><i>无需捕获处理的异常</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午2:37:22</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午2:37:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class UncheckedException extends BaseException {
	private static final long serialVersionUID = -5221776051533352501L;

	/**
	 * @param message  异常信息
	 */
	public UncheckedException(String message) {
		super(message);
	}

	/**
	 * @param cause  异常原因
	 */
	public UncheckedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message 异常信息
	 * @param cause  异常原因
	 */
	public UncheckedException(String message, Throwable cause) {
		super(message, cause);
	}
}
