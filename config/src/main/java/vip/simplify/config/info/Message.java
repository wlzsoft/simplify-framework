package vip.simplify.config.info;

import vip.simplify.exception.MessageException;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.utils.StringUtil;

/**
 * <p><b>Title:</b><i>信息提示</i></p>
 * <p>Desc: 支持两种方式(对比两种方式的性能开销)：A.通过ThreadLocal来实现.B.通过异常机制实现信息提示,支持i18n国际化,如下：
 *          注意：1.异常机制[因为使用了sync同步，并且需要填充构建线程堆栈信息]，所以性能低下，大并发下表现更明显，如果是高并发业务，需要慎重考虑使用
 *                2.异常机制可以使的代码的简洁性和通用性得到提升，所以又不应该在业务处理时完全抛弃掉
 *                3.可以考虑使用代码生成来替换掉异常构建，类似controller的methodinvokegen
 *          异常问题优化方案：1.对MessageException做优化，具体看这个异常类
 *                           2.使用异常对象池的概念来解决频繁创建异常对象的开销
 *                           3.使用ThreadLocal来解决异常非通用信息的传递</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月2日 下午4:56:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月2日 下午4:56:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class Message {
	
	/**
	 * 待测试 TODO
	 * 方法用途: 获取配置文件中的提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values 信息变量，可以有多个 
	 * @return
	 */
	public static String get(String code, Object... values) {
		MessageConfig messageConfig = BeanFactory.getBean(MessageConfig.class);
		String message = (String) messageConfig.getProp().get(code);
		message = StringUtil.format(message, values);
		return message;
	}

	/**
	 *
	 * 方法用途: 正常提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个
	 */
	public static void info(String code, Object... values) {
		info(get(code, values));
	}
	/**
	 *
	 * 方法用途: 警告信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个
	 */
	public static void warn(String code, Object... values) {
		warn(get(code, values));
	}
	/**
	 *
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个
	 */
	public static void error(String code, Object... values) {
		error(get(code, values));
	}

	///异常处理方式

	/**
	 * 
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param ex 异常对象
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个 
	 */
	public static void error(Throwable ex, String code, Object... values) {
		MessageException messageException = new MessageException(500,get(code, values),ex);
		throw messageException;
	}
	/**
	 * 
	 * 方法用途: 正常提示信息<br>
	 * 操作步骤: TODO<br>
	 * @param message 错误信息
	 */
	public static void info(String message) {
		MessageException messageException = new MessageException(208,message);
		throw messageException;
	}
	/**
	 * 
	 * 方法用途: 警告信息<br>
	 * 操作步骤: TODO<br>
	 * @param message 错误信息
	 */
	public static void warn(String message) {
		MessageException messageException = new MessageException(300,message);
		throw messageException;
	}
	/**
	 * 
	 * 方法用途: 警告信息<br>
	 * 操作步骤: TODO<br>
	 * @param message 错误信息
	 */
	public static void error(String message) {
		MessageException messageException = new MessageException(500,message);
		throw messageException;
	}
	
	/**
	 * 方法用途: 警告信息<br>
	 * 操作步骤: TODO<br>
	 * @param statusCode 自定义的状态码-http协议的状态码
	 * @param message 错误信息
	 */
	public static void error(Integer statusCode,String message) {
		MessageException messageException = new MessageException(statusCode,message);
		throw messageException;
	}

	///ThreadLocal处理方式
	/**
	 *
	 * 方法用途: 错误提示信息<br>
	 * 操作步骤: 调用这个方法后，必须return<br>
	 * @param ex 异常对象
	 * @param code 信息编码
	 * @param values  信息变量，可以有多个
	 */
	public static void errorT(Throwable ex, String code, Object... values) {
		MessageException messageException = new MessageException(500,get(code, values),ex);
		MessageThreadLocal.threadLocal.set(messageException);
	}
	/**
	 *
	 * 方法用途: 正常提示信息<br>
	 * 操作步骤: 调用这个方法后，必须return<br>
	 * @param message 错误信息
	 */
	public static void infoT(String message) {
		MessageThreadLocal.info(message);
	}
	/**
	 *
	 * 方法用途: 警告信息<br>
	 * 操作步骤: 调用这个方法后，必须return<br>
	 * @param message 错误信息
	 */
	public static void warnT(String message) {
		MessageThreadLocal.warn(message);
	}
	/**
	 *
	 * 方法用途: 警告信息<br>
	 * 操作步骤: 调用这个方法后，必须return<br>
	 * @param message 错误信息
	 */
	public static void errorT(String message) {
		MessageThreadLocal.error(message);
	}
}
