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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
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
	 * 操作步骤: 可以是一个或多个地址
	 *           如果default为空字符串，那么就会在解析时通过类名和方法名来约定构建path地址。
	 *           构建规制是：[/类名/方法名] 其中类名的第一个字母小写，并且不包含Controller后缀<br>
	 * @return
	 */
	String[] path() default "";

	/**
	 *
	 * 方法用途: 静态化设置<br>
	 * 操作步骤: 1.如果为true，说明开启静态化，那么模版文件的相对动态内容不会被解析，不会执行controller
	 *           2.可以配合WebCache注解一起使用，加速访问，减少io
	 *           3.通过类似nginx的前端反向代理做缓存，减少web服务请求
	 *           4.再通过cdn加速(回源读取加速)<br>
	 * @return
	 */
	boolean isStatic() default false;
}