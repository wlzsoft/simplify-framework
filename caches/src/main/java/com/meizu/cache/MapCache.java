package com.meizu.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.annotation.CacheEvict;
import com.meizu.cache.annotation.CachePut;
import com.meizu.cache.annotation.Cacheable;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: 考虑新标准JSR-107(jCache)的实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月19日 下午2:22:11</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月19日 下午2:22:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Bean(type=BeanTypeEnum.SINGLE)
public class MapCache<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	//TODO 需要优化，通过内存系统来存储和获取，后续把这个map单独抽取成一个类，在类中，map的来源是通过缓存配置得到的
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
	@SuppressWarnings("unchecked")
	//	@Cacheable(value="sys_map", key="caches[1].name")
//	@CachePut(value = "sys_map", key = "#menu.id",condition = "#sys_map != null")
//	@CachePut(value = {"sys_map1","sys_map2"}, key = "#key",condition = "#sys_map != null")
//	@CachePut(value = "sys_map", key = "#p0.id",condition = "#sys_map != null")
	@Cacheable(value = "sys_map",key="#key")
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
	@CachePut(value = "sys_map", key = "#key")
	public T put(String key,T perm) {
		map.put(key, perm);
		return perm;
	}
	@CacheEvict(value = "sys_map", key="#key")
	public void remove(String key) {
		map.remove(key);
	}
	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: beforeInvocation 属性默认是 false，如果map的clear方法抛异常，那么清楚缓存失败，如果想确保，无论如何，都清楚缓存成功，那么需要设置为true<br>
	 */
//	@CacheEvict(value = "sys_map", allEntries = true,beforeInvocation=true)
	@CacheEvict(value = "sys_map", allEntries = true)
	public void clear() {
		map.clear();
	}
	
}
