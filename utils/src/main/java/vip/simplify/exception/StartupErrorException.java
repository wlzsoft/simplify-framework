package vip.simplify.exception;

/**
 * <p><b>Title:</b><i>服务启动严重错误异常</i></p>
 * <p>Desc: 注意：如果捕获到这个异常，直接停止服务运行</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午2:37:22</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午2:37:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class StartupErrorException extends BaseException {
	private static final long serialVersionUID = -5221776051533352501L;

	/**
	 * @param message  异常信息
	 */
	public StartupErrorException(String message) {
		super(message);
	}

	/**
	 * @param target  目标异常
	 */
	public StartupErrorException(Throwable target) {
		super(target);
	}

	/**
	 * @param message 异常信息
	 * @param target  目标异常
	 */
	public StartupErrorException(String message, Throwable target) {
		super(message, target);
	}
}
