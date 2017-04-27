package vip.simplify.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import vip.simplify.utils.enums.DateFormatEnum;

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
	public void formatAll() {
		System.out.println(DateUtil.formatAll(new Date()));
	}
	
	@Test
	public void format() {
		System.out.println(DateUtil.format(new Date(),DateFormatEnum.YEAR_TO_MONTH));
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
		Assert.assertEquals("2016-06-13 00:00:00",DateUtil.format(DateUtil.getFirstDayOfWeek("2016-06-16")));
	}
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getWeekTime(){
		System.out.println(DateUtil.format(DateUtil.getDayOfChinaWeek(0)));
	}
	@Test
	public void addDay(){
		Assert.assertEquals("2016-06-17 00:00:00",DateUtil.format(DateUtil.addDay(DateUtil.parse("2016-06-15 00:00:00"),2)));
	}
	@Test
	public void addDay2(){
		Assert.assertEquals("2016-06-17 00:00:00",DateUtil.format(DateUtil.addDay(DateUtil.parse("2016-06-15"),2)));
	}
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getSimpleDateRangeOfWeek(){
		String[] arr = DateUtil.getDateRangeOfWeek();
		for (String string : arr) {
			System.out.println(string);
		}
	}
	
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getFirstDayOfMonth(){
		System.out.println(DateUtil.format(DateUtil.getFirstDayOfMonth()));
	}
	
	/**
	 * 方法用途: 已测试<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	public void getLastDayOfMonth(){
		System.out.println(DateUtil.format(DateUtil.getLastDayOfMonth()));
	}
	@Test
	public void getLastDayOfMonthByYearAndMonth(){
		System.out.println(DateUtil.format(DateUtil.getLastDayOfMonth(2016,5)));
	}
	@Test
	public void getFirstDayOfMonthByYearAndMonth(){
		System.out.println(DateUtil.format(DateUtil.getFirstDayOfMonth(2016, 5)));
	}
}
