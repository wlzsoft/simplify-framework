package com.meizu.simplify.utils.serial;

import java.io.IOException;

import org.junit.Test;

import com.meizu.simplify.stresstester.StressTestUtils;
import com.meizu.simplify.stresstester.core.StressTask;
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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class FstSerialTest {
	@Test
	public void testFstSet() {
		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				ISerialize serial = new FstSerialize();
				byte[] barray = serial.serialize(usr);
				User object = serial.unserialize(barray);
				System.out.println(object.getName());
				return null;
			}
		});
	}
	@Test
	public void testFstDataIntegrityCheck() throws IOException, ClassNotFoundException {
		// 对字符串Hello World进行反序列化
		String str = "Hello World !";
		ISerialize serial = new Hessian2Serialize();
		byte[] barray = serial.serialize(str);
		// 此处模拟数据失真，故意只截取序列化结果中的一部分数据
		byte[] copy = new byte[barray.length - 5];
		System.arraycopy(barray, 0, copy, 0, copy.length);

		// 对失真的序列化结果进行反序列化,用于检测反序列化时是否能成功，是失真后的结果，或是抛异常
		String object = serial.unserialize(copy);
		System.out.println(object);
	}
}
