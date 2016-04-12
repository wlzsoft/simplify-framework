package com.meizu.simplify.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.meizu.simplify.exception.UncheckedException;

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
	
}
