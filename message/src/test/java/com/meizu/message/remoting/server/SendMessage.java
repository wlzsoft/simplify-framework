package com.meizu.message.remoting.server;

import org.junit.Before;
import org.junit.Test;

import com.meizu.message.core.RabbitTemplate;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月26日 下午5:24:07</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年5月26日 下午5:24:07</p>
 * @author <a href="mailto:meizu@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 0.1
 *
 */
public class SendMessage {

	@Before
	public void init(){
		Startup.start();
	}
	
	@Test
	public void test() {
		try {
			RabbitTemplate temp = BeanFactory.getBean(RabbitTemplate.class);
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				TestEntity entity = new TestEntity();
				entity.setName("444dfdf中文"+i);
				entity.setDddd("f44asdfd"+i);
				entity.setUserName("44测试"+i);
				temp.send(entity);
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
