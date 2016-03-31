package com.meizu.simplify.config;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;


/**
 * <p><b>Title:</b><i>信息提示</i></p>
 * <p>Desc: 通过异常机制实现信息提示</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月2日 下午4:56:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月2日 下午4:56:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Message {
	
	/**
	 * 获取指定编码的信息
	 * 
	 * @param code 信息编码
	 * @param vars 信息变量
	 * @return 返回指定编码的信息。
	 */
	public String get(String code, Object... vars) {
		return null;
	}

	/**
	 * 抛出业务异常
	 * 
	 * @param code 信息编码
	 * @param vars 信息变量
	 */
	public void error(String code, Object... vars) {
		throw new UncheckedException(get(code, vars));
	}

	/**
	 * 抛出业务异常
	 * 
	 * @param ex 异常对象
	 * @param code 信息编码
	 * @param vars 信息变量
	 */
	public void error(Throwable ex, String code, Object... vars) {
		throw new UncheckedException(get(code, vars));
	}
	
}
