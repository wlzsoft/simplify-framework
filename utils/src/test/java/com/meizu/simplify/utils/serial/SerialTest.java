package com.meizu.simplify.utils.serial;

import org.junit.Test;
import org.nustaq.serialization.FSTConfiguration;

import com.meizu.simplify.stresstester.StressTestUtils;
import com.meizu.simplify.stresstester.core.StressTask;
import com.meizu.simplify.utils.SerializeUtil;
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
public class SerialTest {
	@Test
	public void testFstSet() {
		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
				byte barray[] = conf.asByteArray(usr);
				User object = (User) conf.asObject(barray);
				System.out.println(object.getName());
				return null;
			}
		});
	}
	@Test
	public void testDefaulSet() {
		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				ISerialize serial = new DefaultSerialize();
				byte barray[] = serial.serialize(usr);
				User object = serial.unserialize(barray);
				System.out.println(object);
				return null;
			}
		});
		
	}
	
	@Test
	public void testJDK() {
		ISerialize serial = new DefaultSerialize();
		byte barray[] = serial.serialize("A82F7B6F9308AE4BB165CD03579DD7E3");
//		[-84, -19, 0, 5, 116, 0, 32, 65, 56, 50, 70, 55, 66, 54, 70, 57, 51, 48, 56, 65, 69, 52, 66, 66, 49, 54, 53, 67, 68, 48, 51, 53, 55, 57, 68, 68, 55, 69, 51]
//		[-84, -19, 0, 5, 116, 0, 32, 65, 56, 50, 70, 55, 66, 54, 70, 57, 51, 48, 56, 65, 69, 52, 66, 66, 49, 54, 53, 67, 68, 48, 51, 53, 55, 57, 68, 68, 55, 69, 51]
		String object = serial.unserialize(barray);
		System.out.println(object);
		
	}
	
	@Test
	public void testSerialUtil() {
		User usr = new User("101001", "testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		
		StressTestUtils.testAndPrint(1000, 10000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
				byte barray[] = SerializeUtil.serialize(usr);
				User object = SerializeUtil.unserialize(barray);
				System.out.println(object.getName());
				return null;
			}
		});
		
	}
	@Test
	public void testDefaul() {
		User bean = new User("3434","哈哈");
	    bean.setUsername("xxxxx");
	    bean.setPassword("123456");
	    bean.setAge(1000000);
	    System.out.println("序列化 ， 反序列化 对比测试：");
	    long size = 0;
	    long time1 = System.currentTimeMillis();
	    ISerialize serial = new DefaultSerialize();
	    for (int i = 0; i < 10000; i++) {
	      byte[] jdkserialize = serial.serialize(bean);
	      size += jdkserialize.length;
	      serial.unserialize(jdkserialize);
	    }
	    System.out.println("原生序列化方案[序列化10000次]耗时："
	        + (System.currentTimeMillis() - time1) + "ms size:=" + size);

	    size = 0;
	    long time2 = System.currentTimeMillis();
	    serial = new FstSerialize();
	    for (int i = 0; i < 10000; i++) {
	      byte[] serialize = serial.serialize(bean);
	      size += serialize.length;
	      User u = serial.unserialize(serialize);
	      System.out.println(u.getName());
	    }
	    System.out.println("fst序列化方案[序列化10000次]耗时："
	        + (System.currentTimeMillis() - time2) + "ms size:=" + size);
	    
	    size = 0;
	    long time3 = System.currentTimeMillis();
	    serial = new KryoSerialize();
	    for (int i = 0; i < 10000; i++) {
	      byte[] serialize = serial.serialize(bean);
	      size += serialize.length;
	      User u = serial.unserialize(serialize);
	      System.out.println(u.getName());
	    }
	    System.out.println("kryo序列化方案[序列化10000次]耗时："
	        + (System.currentTimeMillis() - time3) + "ms size:=" + size);
	    
	    size = 0;
	    long time4 = System.currentTimeMillis();
	    ISerialize serial2 = new Hessian2Serialize();
	    for (int i = 0; i < 10000; i++) {
	      byte[] serialize = serial2.serialize(bean);
	      size += serialize.length;
	      User u = serial2.unserialize(serialize);
	      System.out.println(u.getName());
	    }
	    System.out.println("Hessian2序列化方案[序列化10000次]耗时："
	        + (System.currentTimeMillis() - time4) + "ms size:=" + size);

		
	}
}
