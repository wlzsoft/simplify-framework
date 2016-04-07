package com.meizu.simplify.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>请求地址解析映射</i></p>
 * <p>Desc: 可在controller重复标注requestMap注解：
 *      <pre><code>
        @RequestMaps({@RequestMap("test"), @RequestMap("test2")})
		public Test test() {
		}
		@RequestMap("test")
        @RequestMap("test2")
        public Test test() {
		}
		</code></pre></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午7:17:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午7:17:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Repeatable(RequestMaps.class)
public @interface RequestMap {
	/**
	 * 
	 * 方法用途: 请求映射地址设置<br>
	 * 操作步骤: 可以是一个或多个地址<br>
	 * @return
	 */
	String[] path();
}