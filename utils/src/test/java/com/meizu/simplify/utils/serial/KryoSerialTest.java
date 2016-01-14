package com.meizu.simplify.utils.serial;

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;

import com.meizu.simplify.utils.entity.User;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午7:36:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午7:36:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class KryoSerialTest {
	@Test
	public void testFstSet() {
		FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");

		byte barray[] = conf.asByteArray(usr);
		User object = (User) conf.asObject(barray);
		System.out.println(object.getName());
	}
}
