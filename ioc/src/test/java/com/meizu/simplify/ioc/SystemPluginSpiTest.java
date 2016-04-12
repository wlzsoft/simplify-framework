package com.meizu.simplify.ioc;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Test;

import com.meizu.simplify.spi.ISystemPlugin;

/**
 * <p><b>Title:</b><i>spi插件机制测试</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月21日 下午4:58:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月21日 下午4:58:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SystemPluginSpiTest {

	@Test
    public  void test() {
    	ServiceLoader<ISystemPlugin> operations = ServiceLoader.load(ISystemPlugin.class);
 		Iterator<ISystemPlugin> operationIterator = operations.iterator();
 		while (operationIterator.hasNext()) {
 			ISystemPlugin spi = operationIterator.next();//com.meizu.simplify.spi.IReloadPlugin
 		}
    }
}