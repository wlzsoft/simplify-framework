package vip.simplify.utils;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

/**
  * <p><b>Title:</b><i>类工具测试类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年10月8日 上午11:32:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年10月8日 上午11:32:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ClassUtilTest {
	@Test
	public void testBaseType() {
		//判断是否是基本类型
		Class<?> c = int.class;
		Assert.assertEquals(true,c.isPrimitive());
		//测试Integer.TYPE 返回基本类型的class是否正确，这里返回的基本类型的class应该是等于int.class
		Assert.assertEquals(int.class, Integer.TYPE);
		//判断是否基本类型包装类型 Integer ,Long, Double
		try {
			Field field = Integer.class.getField("TYPE");//NoSuchFieldException 字段不存在，这个异常会抛出
			Object obj = field.get(null);
			boolean isPrimitive = ((Class<?>)obj).isPrimitive();
			Assert.assertEquals(isPrimitive, true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} catch (IllegalArgumentException | IllegalAccessException  | SecurityException e) {
			Assert.assertFalse(true);
//			e.printStackTrace();
		}
	}
}
