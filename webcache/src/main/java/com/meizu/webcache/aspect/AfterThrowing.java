package com.meizu.webcache.aspect;
/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 上午11:28:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 上午11:28:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public @interface AfterThrowing {

	String throwing();

	String pointcut();

}
