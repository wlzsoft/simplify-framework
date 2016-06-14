package com.meizu.simplify.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月23日 下午4:09:47</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月23日 下午4:09:47</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class DateUtilTest {

	@Test
	public void format() {
		System.out.println(DateUtil.formatAll(new Date()));
	}
	
	@Test
	public void getDayOfWeekByToday(){
		System.out.println(DateUtil.getDayOfWeekByToday());
	}
	@Test
	public void getWeekByToday(){
		System.out.println(DateUtil.getWeekByToday());
	}
	@Test
	public void getDayOfWeek(){
		Date date = DateUtil.parse("2016-06-13 10:10:10");
		int resultDay = DateUtil.getDayOfWeek(date);
		Assert.assertEquals(2, resultDay);
	}
	/**
	 * 方法用途: 未测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getWeekTime(){
		System.out.println(DateUtil.format(DateUtil.getWeekTime(0)));
	}
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getFirstDayOfWeek(){
		System.out.println(DateUtil.format(DateUtil.getFirstDayOfWeek(new Date())));
	}
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getFirstDayOfWeekStr(){
		System.out.println(DateUtil.format(DateUtil.getFirstDayOfWeek("2016-06-12")));
	}
	/**
	 * 方法用途: 未测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getSimpleDateRangeOfWeek(){
		String[] arr = DateUtil.getSimpleDateRangeOfWeek();
		for (String string : arr) {
			System.out.println(string);
		}
	}
}
