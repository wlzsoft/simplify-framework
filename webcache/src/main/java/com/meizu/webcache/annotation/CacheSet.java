package com.meizu.webcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p><b>Title:</b><i> 缓存模式</i></p>
 * <p>Desc: 注意： 使用它会导致xss安全控制失效
 * 
 * 
 * 缓存方面：提供缓存管理后台，可以监控页面缓存，通过action或是url查询缓存，通过dubbo删除指定服务缓存，或是对应url所有服务缓存，需配置dubbo
监控服务监控：通过消息服务来监控预警和查询服务状态，通过tcp协议或是http，如果是tcp，还要确定是否可发送心跳数据到具体action，而不单单ping通，不依赖nginx

 * </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:04:04</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:04:04</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheSet {
	
	/**
	 * 方法用途:  缓存模式
	 * 操作步骤: TODO<br>
	 * @return Mem(内存模式) File(文件模式, 默认存储位置在webinfo下的htmlCache目录下)，浏览器端缓存（现代浏览器采用html5来存储，否则使用cookie）
	 */
	CacheMode mode();
	
	/**
	 * 方法用途: 删除空格，压缩减少磁盘空间或内存空间的占用<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	boolean removeSpace() default false;
	
	/**
	 * 方法用途: 存活时间<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	int timeToLiveSeconds() default 3600;
    /**
     * 
     * 方法用途: 是否同时开启浏览器端缓存<br>
     * 操作步骤: TODO<br>
     * @return
     */
    boolean enableBrowerCache() default false;
	public enum CacheMode {
		Mem, File, nil, browser
	}
	
}