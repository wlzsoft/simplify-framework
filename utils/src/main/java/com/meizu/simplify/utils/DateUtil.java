package com.meizu.simplify.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.meizu.simplify.utils.enums.DateFormatEnum;

/**
  * <p><b>Title:</b><i>日期操作工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午7:42:12</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午7:42:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class DateUtil {
	
	/**
	 * 按日期格式创建初始化SimpleDateFormat对象，避免每次格式化日期的时候都创建一次SimpleDateFormat
	 */
	/*private static final Map<DateFormatEnum,SimpleDateFormat> simpleDateFormatMap = new EnumMap<>(DateFormatEnum.class);
	static {
		DateFormatEnum[] dfeArr = DateFormatEnum.values();
		for (int i=0; i < dfeArr.length; i++) {
			DateFormatEnum dateFormatEnum = dfeArr[i];
			simpleDateFormatMap.put(dateFormatEnum, new SimpleDateFormat(dateFormatEnum.value()));
		}
	}*/
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为[yyyy-MM-dd HH:mm:ss]格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, DateFormatEnum.YEAR_TO_SECOND);
	}
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为[yyyy-MM-dd HH:mm:ss.SSS]格式字符串<br>
	 * 操作步骤: 全日期格式 考虑到微妙级别,更精准的日期格式 <br>
	 * @param date
	 * @return
	 */
	public static String formatAll(Date date) {
		return format(date, DateFormatEnum.YEAR_TO_MILLISECOND);
	}
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为指定格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date  java.util.Date格式
	 * @param pattern yyyy-MM-dd HH:mm:ss | yyyy年MM月dd日 HH时mm分ss秒
	 * @return
	 */
	public static String format(Date date, DateFormatEnum pattern) {
		if (date == null) {
			return null;
		}
		LocalDateTime localDateTime = getLocalDateTime(date);
		String dateStr = localDateTime.format(DateTimeFormatter.ofPattern(pattern.value()));
		/*try {
			dateStr = getDateFormat(pattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return dateStr;
	}
	
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为指定格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @param pattern
	 * @return
	 * @author wanghaibin 2016/4/7
	 */
	public static String format(long timesamp, DateFormatEnum pattern) {
		return format(new Date(timesamp),pattern);
	}
	
	
    /**
     * 
     * 方法用途: 获取SimpleDateFormat对象<br>
     * 操作步骤: TODO<br>
     * @param type
     * @return
     */
   /*private static SimpleDateFormat getDateFormat(DateFormatEnum pattern) {
        SimpleDateFormat sdf = simpleDateFormatMap.get(pattern);//TODO SimpleDateFormat,java.util.Date 是线程不安全的 ,每次都用new的才安全，这里的用法可能会导致数据被其他线程篡改
        //TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");//TimeZone.getTimeZone("GMT+8");
        //sdf.setTimeZone(zone);
        return sdf;
    }*/
    
    /**
     * 方法用途: date转换为LocalDateTime<br>
     * 操作步骤: TODO<br>
     * @param date
     * @return
     */
    private static LocalDateTime getLocalDateTime(Date date) {
    	Instant instant = date.toInstant();
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
		LocalDateTime localDateTime = zdt.toLocalDateTime();
    	return localDateTime;
    }
    
    /**
     * 方法用途: LocalDateTime转换为date<br>
     * 操作步骤: TODO<br>
     * @param localDateTime
     * @return
     */
    private static Date getDate(LocalDateTime localDateTime) {
		Date defaultValue;
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		Instant instant = zonedDateTime.toInstant();
		defaultValue = Date.from(instant);
		return defaultValue;
	}
	
    /**
     * 
     * 方法用途: 转换日期字符串格式.即将'/','-'替换成当前日期分隔符<br>
     * 操作步骤: TODO<br>
     * @param dateString 
     * @return 
     */
    public static String correct(String dateString) {
        String resultString = dateString;
        if (dateString.indexOf("/") > -1) {
            resultString = dateString.replace("/", DateFormatEnum.Limiter.DATE_LIMITER.toString());
        }
        if (dateString.indexOf("-") > -1) {
            resultString = dateString.replace("/", DateFormatEnum.Limiter.DATE_LIMITER.toString());
        }
        return resultString;
    }
    
    /**
	 * 
	 * 方法用途: 获取当前日期对象<br>
	 * 操作步骤: TODO<br>
	 * @return
	 * @author whb 2016/4/7
	 */
	public static Date getCurrentDate() {
		Date date = new Date();
		return date;
	}
//	====================日期格式解析：时间字符串转时间对象===================
	/**
	 * 
	 * 方法用途: 时间字符串转时间，转失败返回默认<br>
	 * 操作步骤: TODO<br>
	 * @param dateStr 时间字符串
	 * @param pattern yyyy-MM-dd HH:mm:ss 或是 yyyy-MM-dd | yyyy年MM月dd日 HH时mm分ss秒
	 * @param defaultValue
	 * @return
	 */
	public static Date parse(String dateStr, DateFormatEnum pattern, Date defaultValue){
		if(StringUtil.isEmpty(dateStr)){
			return defaultValue;
		}
		LocalDateTime localDateTime = null;
		if(dateStr.length()==10||dateStr.length() == 7) {
			LocalDate localDate = LocalDate.parse(dateStr,DateTimeFormatter.ofPattern(pattern.value()));
			LocalTime localTime = LocalTime.of(0, 0, 0); 
			localDateTime = localDate.atTime(localTime);
		} else {
			localDateTime = LocalDateTime.parse(dateStr,DateTimeFormatter.ofPattern(pattern.value()));
		}
		defaultValue = getDate(localDateTime);
		/*try {
			SimpleDateFormat sdf = getDateFormat(pattern);
			Date date = sdf.parse(dateString);
			return date;
		} catch (Exception e) {
			System.err.println("解析日期字符串错误:"+e.getMessage());
		}*/
		return defaultValue;
		
	}
	
	/**
	 * 
	 * 方法用途: 据参数格式化字符串为日期<br>
	 * 操作步骤: TODO<br>
	 * @param dateStr 字符串形式的日期
	 * @param pattern 格式化格式
	 * @return 根据字符串格式化后的日期
	 */
	public static Date parse(String dateStr, DateFormatEnum pattern) {
		return parse(dateStr,pattern,null);
	}
	
	/**
	 * 方法用途: 根据时间字符串长度，来判断格式化的格式[全格式为：yyyy-MM-dd HH:mm:ss.SSS]，有匹配长度时返回格式为 yyyy-MM-dd HH:mm:ss.SSS 的时间格式<br>
	 * 操作步骤: TODO<br>
	 * @param dateStr 字符串形式的日期
	 * @return 根据字符串格式化后的日期
	 */
	public static Date parse(String dateStr) {
		Date ret = null;
		switch (dateStr.length()) {
			case 10:
				ret = parse(dateStr, DateFormatEnum.YEAR_TO_DAY);
				break;
			case 16:
				ret = parse(dateStr, DateFormatEnum.YEAR_TO_MINUTE);
				break;
			case 19:
				ret = parse(dateStr, DateFormatEnum.YEAR_TO_SECOND);
				break;
			case 13:
				ret = parse(dateStr, DateFormatEnum.YEAR_TO_HOUR);
				break;
			case 7:
				ret = parse(dateStr, DateFormatEnum.YEAR_TO_MONTH);
				break;
			default:
				ret = parse(dateStr, DateFormatEnum.DAY_TO_MILLISECOND);
		}
		return ret;
	}
//	====================日期格式解析：时间字符串转另外一个格式的时间字符串===================
	
	/**
	 * 
	 * 方法用途: 时间字符串格式转换<br>
	 * 操作步骤: TODO<br>
	 * @param dateStr 时间字符串
	 * @param format 时间字符串的格式
	 * @param toFormat 格式为的格式
	 * @return
	 */
	public static String parseAndFormat(String dateStr, DateFormatEnum format, DateFormatEnum toFormat) {
		return format(parse(dateStr, format), toFormat);
	}
//	=========================周，星期的日期处理==============================
	
    /**
     * 方法用途: 获取当天是一周的第几天 周日为一周的第一天<br>
     * 操作步骤: 注意：一般不会直接使用这个方法，而会使用getWeekByToday方法<br>
     * @return 一周的第几天
     */
    public static int getDayOfWeekByToday(){
        return getDayOfWeek(new Date());
	}
    
    /**
     * 方法用途: 获取当天是周几<br>
     * 操作步骤: TODO<br>
     * @return 周几  sunday,monday,tuesday,wednesday,thursday,friday,saturday
     */
    public static int getWeekByToday(){
    	int daynum = getDayOfWeekByToday();
    	if(daynum == 1) {
    		return 7;
    	} else {
    		return getDayOfWeekByToday()-1;
    	}
    }

    /**
     * 方法用途: 获取某天是一周的第几天 周日为一周的第一天<br>
     * 操作步骤: TODO<br>
     * @param date 指定的日期：也即是 "某天"
     * @return 一周的第几天
     */
    public static int getDayOfWeek(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
//    	calendar.setTimeInMillis(time);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    
	/**
	 * 方法用途: 获取某天是一周的第几天 周日为一周的第一天<br>
	 * 操作步骤: TODO<br>
	 * @param dateStr 指定的日期字符串：也即是 "某天"
	 * @return
	 */
	public static int getDayOfWeek(String dateStr) {
		Date date = parse(dateStr);
		return getDayOfWeek(date);
	}

    /**
     * 方法用途: 获取某天是一周的第几天 周日为一周的第一天<br>
     * 操作步骤: TODO<br>
     * @param time 时间搓：某天的时间搓
     * @return
     */
    public static int getDayOfWeek(long time){
        return getDayOfWeek(new Date(time));
    }
    
    /**
     * 方法用途: 获取某天所在周的第一天<br>
     * 操作步骤: TODO<br>
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {//特殊处理星期天是第一天的情况！按中国人习惯把周1看成第一天，星期天看成最后一天
            calendar.add(Calendar.DAY_OF_WEEK, -1);//扣除一天
        }
        calendar.set(Calendar.MILLISECOND,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
        return calendar.getTime();
    }

	/**
	 * 方法用途: 获取某天所在周的第一天<br>
	 * 操作步骤: 注意：1.以下是jdk1.8的实现方式，LocalDate区别于Date，是线程安全的，并且LocalDate只包含日期，另外的LocalTime只包含时间，这里用不上
	 *                2.后续日期的操作全部换成jdk1.8的实现方式
	 * <br>
	 * @param dateStr 格式yyyy-MM-dd
	 * @return
	 */
	public static Date getFirstDayOfWeek(String dateStr) {
		LocalDate localDate = LocalDate.parse(dateStr);
//		TemporalField fieldISO = WeekFields.of(localDate.getDayOfWeek(), 1).dayOfWeek();
		TemporalField fieldISO = WeekFields.of(Locale.CHINA).dayOfWeek();
		localDate = localDate.minusDays(1)//特殊处理星期天是第一天的情况！按中国人习惯把周1看成第一天，星期天看成最后一天,所以扣除一天
				             .with(fieldISO, Calendar.MONDAY);
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
    
	/**
	 * 方法用途: 获取某周的周一对应的日期<br>
	 * 操作步骤: TODO未测试<br>
	 * @param weekCount 0 本周 1下周 n下n周
	 * @return 周一对应的日期
	 */
	public static Date getDayOfWeek(int weekCount){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, weekCount);
		calendar.set(Calendar.MILLISECOND,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		return calendar.getTime();
	}
	
	/**
	 * 方法用途: 在某天的基础上增加几天或减少几天<br>
	 * 操作步骤: TODO<br>
	 * @param sDate
	 * @param iDay
	 * @param sformat
	 * @return
	 */
	public static Date addDay(Date date, int dayCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, dayCount);
		return calendar.getTime();
	}
	/**
	 * 方法用途: 在某天的基础上增加几天或减少几小时<br>
	 * 操作步骤: TODO<br>
	 * @param sDate
	 * @param iDay
	 * @param sformat
	 * @return
	 * @author wanghb 20160810
	 */
	public static Date addOrSubHour(Date date, int hourCount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hourCount);
		return calendar.getTime();
	}
	
	/**
	 * 方法用途: 返回本周的区间的格式化的日期对，格式为yyyy-MM-dd HH:mm:ss<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String[] getDateRangeOfWeek() {
		Date curDate = new Date();
		Date fisrtDate = getFirstDayOfWeek(curDate);
		String fisrtDateStr = format(fisrtDate);
		Date endDate = addDay(fisrtDate, 6);
		String endDateStr = format(endDate);
		String DateRang[] = new String[2];
		DateRang[0] = fisrtDateStr;
		DateRang[1] = endDateStr;
		return DateRang;
	}
	/**
	 * 方法用途: 获取下周开始日期与结束日期<br>
	 * 操作步骤: TODO<br>
	 * @param dt1 
	 * @param dt2 
	 * @return 1:dt1 在dt2前;-1:dt1在dt2后;0:相等
	 * @author wanghb 20160810
	 */
	public static String[] getNextDateRangeOfWeek() {
		Date curDate = getDayOfWeek(1);
		Date fisrtDate = getFirstDayOfWeek(curDate);
		String fisrtDateStr = format(fisrtDate);
		Date endDate = addDay(fisrtDate, 6);
		String endDateStr = format(endDate);
		String DateRang[] = new String[2];
		DateRang[0] = fisrtDateStr;
		DateRang[1] = endDateStr;
		return DateRang;
	}
//	=========================================月份相关日期操作============================================
	
	/**
	 * 
	 * 方法用途: 获取本月的第一天日期<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		return c.getTime();
	}
	
	 /**
     * 方法用途: 根据年和月获取所给的月份的第一天日期<br>
     * 操作步骤: TODO<br>
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }
    
    /**
	 * 方法用途: 获取本月的最后一天日期<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		return getLastDayOfMonth(new Date());
	}
	
	/**
	 * 
	 * 方法用途: 获取本月的最后一天日期<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
//		c.setTimeInMillis(c.getTimeInMillis() - (24 * 3600 * 1000));
		return c.getTime();
	}
	
    /**
     * 方法用途: 根据年和月获取所给的月份的最后一天日期<br>
     * 操作步骤: TODO<br>
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
//      int maxDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxDate = getActualMaximum(year,month);
        c.set(Calendar.DAY_OF_MONTH, maxDate);
        return c.getTime();
    }
    
	/**
	 * 方法用途: 获得当前时间是这个月的几号<br>
	 * 操作步骤: TODO<br>
	 * @param time
	 * @return
	 */
	public static int getDayOfMonth(long time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 方法用途: 根据年和月获取所给的月份的天数<br>
	 * 操作步骤: TODO<br>
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static int getActualMaximum(int year,int month){
		int endDay=0;
		if(month<1||month>12||year<1753||year>9999) {
			return 0;
		}
		switch(month){
			case 4:
				endDay = 30;
				break;
			case 6:
				endDay = 30;
				break;
			case 9:
				endDay = 30;
				break;
			case 11:
				endDay = 30;
				break;
			case 2:
				if (year % 4 == 0)
					endDay = 29;
				else
					endDay = 28;
				break;
			default:
				endDay = 31;
				break;
		}
		return endDay;
	}
	/**
	 * 方法用途: 比较时间大小<br>
	 * 操作步骤: TODO<br>
	 * @param dt1 
	 * @param dt2 
	 * @return 1:dt1 在dt2前;-1:dt1在dt2后;0:相等
	 * @author wanghb 20160810
	 */
	public static int compareDate(Date dt1,Date dt2){
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {//相等
            return 0;
        }
	}
	/**
	 * 方法用途: 计算两日期相差天数<br>
	 * 操作步骤: TODO<br>
	 * @param beginDate
	 * @param endDate
	 * @return 1:dt1 在dt2前;-1:dt1在dt2后;0:相等
	 * @author wanghb 20160810
	 */
	public static int getDaysSpace(Date beginDate,Date endDate) {
		 Long checkday=0L; 
		 checkday = (endDate.getTime()-beginDate.getTime())/(1000*24*60*60);
		 return checkday.intValue();
	}
	/**
	 * 方法用途:获取年份<br>
	 * 操作步骤: TODO<br>
	 * @param 
	 * @author wanghb 20160810
	 */
	public static int getYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	/**
	 * 方法用途:获取月份<br>
	 * 操作步骤: TODO<br>
	 * @param 
	 * @author wanghb 20160810
	 */
	public static int getMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}
	public static void main(String[] args) {
//		Date date=addOrSubHour(new Date(),7);
		String [] nextWeek=getNextDateRangeOfWeek();
		System.out.println(nextWeek[0]+";"+nextWeek[1]);
	}
	
}
