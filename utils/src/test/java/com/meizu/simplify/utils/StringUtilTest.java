package com.meizu.simplify.utils;

import java.util.ArrayList;
import java.util.List;

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
public class StringUtilTest {
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
	
	@Test
	public void removeHtml() {
		Assert.assertEquals(StringUtil.removeHtmlLabel("  Chuangye <a>sdd<span>333</span></a> lcy  "),"  Chuangye sdd333 lcy  ");
	}
	
	@Test
	public void removeSpace() {
		Assert.assertEquals(StringUtil.removeHtmlSpace("\r\nChuangye lcy"),"Chuangye lcy");
	}
	
	@Test
	public void trim() {
		Assert.assertEquals(StringUtil.trim("  Chuangye lcy  "),"Chuangye lcy");
	}
	@Test
	public void clearHTML() {
		Assert.assertEquals(StringUtil.clearHTML("\r\n<a>Chuangye lcy</a>"),"Chuangye lcy");
	}
	
	@Test
	public void join() {
		List<String> list = new ArrayList<>();
		list.add("sdf");
		list.add("sd23f");
		list.add("sasdf");
		list.add("234sdf");
		String joinStr = StringUtil.join(list, ",");
		Assert.assertEquals("sdf,sd23f,sasdf,234sdf", joinStr);
	}
	
	@Test
	public void joinByNull() {
		List<String> list = new ArrayList<>();
		String joinStr = StringUtil.join(list, ",");
		Assert.assertNull(joinStr);
	}
	
}
