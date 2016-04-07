package com.meizu.simplify.exception;

/**
 * <p><b>Title:</b><i>消息异常</i></p>
 * <p>Desc: 用于最终用户端提示信息
 *          注意：异常不会抛出到控制台中</p>
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
public class MessageException extends BaseException{
	private static final long serialVersionUID = -8437206478990172399L;

	public MessageException(int errorCode, String message, Throwable target) {
		super(errorCode, message, target);
	}

	public MessageException(int errorCode, String message) {
		super(errorCode, message);
	}

}
