package com.meizu.simplify.utils;

import org.junit.Assert;
import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 下午5:47:06</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 下午5:47:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ObjectUtilTest {
	
	@Test
	public void isNull() {
		String str = null;
		Assert.assertTrue(ObjectUtil.isNull(str));
	}
	
	
	@Test
	public void isNulls() {
		String str = null;
		Assert.assertTrue(ObjectUtil.isNull(str,null));
	}
	
	@Test
	public void isBooleanByOtherChar() {
		String str = "asdfasdf";
		Assert.assertFalse(ObjectUtil.isBoolean(str));
	}
	
	@Test
	public void isBooleanByZero() {
		String str = "0";
		Assert.assertFalse(ObjectUtil.isBoolean(str));
	}
	
	@Test
	public void isBooleanByOne() {
		String str = "1";
		Assert.assertFalse(ObjectUtil.isBoolean(str));
	}
	
	@Test
	public void isBoolean() {
		String str = "true";
		Assert.assertTrue(ObjectUtil.isBoolean(str));
	}
	
	@Test
	public void isLong() {
		String str = "1";
		Assert.assertTrue(ObjectUtil.isLong(str));
	}
	
	/**
	 * 方法用途: 参数值相关(引用传递和值传递)<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void isObject() {
		char[] c = new char[]{'h','e','l','l','o'};
		String s = "think";
		modifyVar(s,c);
		System.out.println(new String(c)+"|"+s);
	}
	public void modifyVar(String s,char[] c) {
		s = "okString";
		c[2] = 'm';
	}
	
	@Test
	public void testObject() {
		Test2 test = new TestImpl2();
		test.inoke();
	}
	
	
}
class Test2 {
	static {
		System.out.println("test2-static");
	}
	public Test2() {
		System.out.println("test2-new");
	}
	{
		System.out.println("test2-non");
	}
	public void inoke() {
		System.out.println("test2-inoke");
	}
}
class TestImpl2 extends Test2 {
	static {
		System.out.println("TestImpl2-static");
	}
	{
		System.out.println("TestImpl2-non");
	}
	public TestImpl2() {
//		super("a");//必须调用，如果是super(),无参，那么可以不调用
		System.out.println("TestImpl2-new");
	}
	public void inoke() {
		System.out.println("TestImpl2-inoke");
	}
}