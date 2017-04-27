package vip.simplify.message.remoting.server;

import org.junit.Before;
import org.junit.Test;

import vip.simplify.ioc.Startup;

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
//		try {
//			RabbitTemplate temp = BeanFactory.getBean(RabbitTemplate.class);
//			for (int i = 0; i <3; i++) {
//				TestEntity entity = new TestEntity();
//				entity.setName("t1");
//				entity.setDddd("t1");
//				entity.setUserName("t1");
//				temp.send("t1",entity);
////				Thread.sleep(3000);
//			}
//			for (int i = 0; i < 3; i++) {
//				TestEntity entity = new TestEntity();
//				entity.setName("tt2");
//				entity.setDddd("tt2");
//				entity.setUserName("tt2测试");
//				temp.send("t2",entity);
////				Thread.sleep(3000);
//			}
//			
//		} catch (Exception e) {
//			System.out.println(e);
//		}
	}

}
