package com.meizu.simplify.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * <p><b>Title:</b><i>基于Map的缓存容器实现</i></p>
 * <p>Desc: TODO和caches模块的MapCache需要合并</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月31日 下午7:04:14</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月31日 下午7:04:14</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MapCache<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public final static  ConcurrentMap<String,Object> map = new ConcurrentHashMap<>();
	public MapCache(){
		logger.info("init");
	}
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: 注意： {"menu1","menu2"} 的方式 ，数据会被缓存在menu1和menu2,中，对使用者完全透明，只是使用就行，好处是： 待总结 TODO
	 *            key属性：1.支持两种方式来获取方法参数，分别是：'#参数名'或者'#p参数index'
	 *                   2.支持spring el 表达式来处理参数值
	 *                   3.除了方法参数，在spring el内部了一个系统变量  root对象，可以通过它来生成key，root对象有如下属性：
	 *                   		属性名称				描述				示例
	 *                   		methodName			当前方法名				#root.methodName
								method				当前方法				#root.method.name
								target				当前被调用的对象			#root.target
								targetClass		 	当前被调用的对象的class	#root.targetClass
								args				当前方法参数组成的数组		#root.args[0]
								caches				当前被调用的方法使用的 Cache	#root.caches[0].name		                   		
						            默认使用root对象时带上#root，可以省略掉#root,因为默认spring就在使用#root,用法是：key="caches[1].name"，或者key="#root.caches[1].name"<br>
	 * @param key
	 * @return
	 */
	public T get(String key) {
		return (T) map.get(key);
	}
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: 注意，这里提供了返回值，是因为只能对返回值做缓存<br>
	 * @param key
	 * @param perm 必须有这个返回值，才能缓存存put的数据
	 * @return
	 */
	public T put(String key,T perm) {
		map.put(key, perm);
		return perm;
	}
	public void remove(String key) {
		map.remove(key);
	}
	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: beforeInvocation 属性默认是 false，如果map的clear方法抛异常，那么清楚缓存失败，如果想确保，无论如何，都清楚缓存成功，那么需要设置为true<br>
	 */
	public void clear() {
		map.clear();
	}
}
