package com.meizu.simplify.utils;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;


/**
 * 
 * <p><b>Title:</b><i>配置文件工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:42:36</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:42:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class PropertieUtilTest {
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: junit老版本没有@Category的时候，会使用TestNG来代替，现在可以直接用junit4来解决，但是junit4的@Category还有有些限制，必须在TestSuite中执行<br>
	 */
	@Test
//	@Category(PropertieUtilTests.class)
	public void test(){
		PropertieUtil util = new PropertieUtil("properties/redis.properties");
		Set<String> set = util.stringPropertyNames();
		for(Iterator<String> it = set.iterator();it.hasNext();){
			System.out.println(it.next());
		}
	}
}
