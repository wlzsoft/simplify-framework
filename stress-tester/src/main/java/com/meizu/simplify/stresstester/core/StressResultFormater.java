package com.meizu.simplify.stresstester.core;

import java.io.Writer;

/**
 * <p><b>Title:</b><i>测试结果内容格式化接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年5月5日 下午4:07:42</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年5月5日 下午4:07:42</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface StressResultFormater {

	/**
	 * 
	 * 方法用途: 格式化<br>
	 * 操作步骤: TODO<br>
	 * @param stressResult
	 * @param writer
	 */
	void format(StressResult stressResult, Writer writer);

}
