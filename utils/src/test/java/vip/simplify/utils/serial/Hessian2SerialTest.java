package vip.simplify.utils.serial;

import org.junit.Test;

import vip.simplify.stresstester.StressTestUtils;
import vip.simplify.stresstester.core.StressTask;
import vip.simplify.utils.entity.User;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午7:36:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午7:36:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Hessian2SerialTest {
	@Test
	public void testHessian2Set() {
		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				ISerialize serial = new Hessian2Serialize();
				byte[] barray = serial.serialize(usr);
				User object = serial.unserialize(barray);
				System.out.println(object.getName());
				return null;
			}
		});
	}
}
