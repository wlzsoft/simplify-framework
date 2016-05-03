package com.meizu.simplify.exception;

/**
 * <p><b>Title:</b><i>消息异常</i></p>
 * <p>Desc: 用于最终用户端提示信息
 *          注意：1.异常不会抛出到控制台中
 *                2.异常单例方式，减少异常创建(异常是存在于线程中，无法如此适用)
 *          </p>
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
public class MessageException extends BaseException{
	private static final long serialVersionUID = -8437206478990172399L;
	public MessageException(int errorCode, String message, Throwable target) {
		super(errorCode, message, target);
	}

	public MessageException(int errorCode, String message) {
		super(errorCode, message);
	}
	/**
	 * 优化A. 不填充堆栈信息，减少性能消耗。带来问题：丢失堆栈信息。解决方法：业务堆栈信息用不到，无需解决
	 * 优化B： 不使用同步，减少线程排队，提高信息。带来问题：如果堆栈多个线程公用的话，会信息不一致问题。解决方法：信息不保存到异常对象中即可，可考虑使用ThreadLocal来解决
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
