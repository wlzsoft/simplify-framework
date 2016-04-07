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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class PropertieUtilTest {
	@Test
	public void test(){
		PropertieUtil util = new PropertieUtil("redis.properties");
		Set<String> set = util.stringPropertyNames();
		for(Iterator<String> it = set.iterator();it.hasNext();){
			System.out.println(it.next());
		}
	}
}
