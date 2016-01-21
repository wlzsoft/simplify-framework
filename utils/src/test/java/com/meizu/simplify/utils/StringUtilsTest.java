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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class StringUtilsTest {
	@Rule  
    public ExpectedException thrown= ExpectedException.none();  
	private String capitalizeExpectMessage = "字符串为空，无法转换为第一个字母为大写";
	
	
	
	@Test
	public void isEmpty() {
		Assert.assertTrue(StringUtil.isEmpty(""));
	}

	@Test
	public void isBlank() {
		Assert.assertTrue(StringUtil.isBlank("    "));
	}
	
	
	@Test
	public void isEmptys() {
		Assert.assertTrue(StringUtil.isEmpty("","","",null));
	}
	
	@Test
	public void isBlanks() {
		Assert.assertTrue(StringUtil.isBlank("    ","   ","",null));
	}
	
	@Test
	public void capitalizeForNull() {
		thrown.expect(UncheckedException.class);  
        thrown.expectMessage(capitalizeExpectMessage);  
		StringUtil.capitalize(null);
	}
	
	@Test
	public void capitalizeForEmpty() {
		thrown.expect(UncheckedException.class);  
        thrown.expectMessage(capitalizeExpectMessage);  
		StringUtil.capitalize("");
	}
	
	@Test
	public void capitalizeForBlank() {
		thrown.expect(UncheckedException.class);  
        thrown.expectMessage(capitalizeExpectMessage);  
		StringUtil.capitalize("  ");
	}
	
	@Test
	public void capitalizeForNotBlank() {
		Assert.assertEquals(StringUtil.capitalize("chuangye lcy"),"Chuangye lcy");
	}
	
	@Test
	public void capitalizeForNotBlankAndTrim() {
		Assert.assertEquals(StringUtil.capitalize(" chuangye lcy "),"Chuangye lcy");
	}
	
	@Test
	public void format() {
		Assert.assertEquals(StringUtil.format("Chuangye{0} lcy{1}","a","b"),"Chuangyea lcyb");
	}
	@Test
	public void formatByNull() {
//		不支持这个格式
//		Assert.assertEquals(StringUtil.format("Chuangye{} lcy{}","a","b"),"Chuangyea lcyb");
	}
	
}
