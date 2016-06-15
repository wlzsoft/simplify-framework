package com.meizu.simplify.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

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
	private static final Map<DateFormatEnum,SimpleDateFormat> simpleDateFormatMap = new EnumMap<>(DateFormatEnum.class);
	static {
		DateFormatEnum[] dfeArr = DateFormatEnum.values();
		for (int i=0; i < dfeArr.length; i++) {
			DateFormatEnum dateFormatEnum = dfeArr[i];
			simpleDateFormatMap.put(dateFormatEnum, new SimpleDateFormat(dateFormatEnum.value()));
		}
	}
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
		try {
			return getDateFormat(pattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
    private static SimpleDateFormat getDateFormat(DateFormatEnum pattern) {
        SimpleDateFormat sdf = simpleDateFormatMap.get(pattern);//TODO SimpleDateFormat,java.util.Date 是线程不安全的 ,每次都用new的才安全，这里的用法可能会导致数据被其他线程篡改
        //TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");//TimeZone.getTimeZone("GMT+8");
        //sdf.setTimeZone(zone);
        return sdf;
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
	 * @param dateString 时间字符串
	 * @param pattern yyyy-MM-dd HH:mm:ss 或是 yyyy-MM-dd | yyyy年MM月dd日 HH时mm分ss秒
	 * @param defaultValue
	 * @return
	 */
	public static Date parse(String dateString, DateFormatEnum pattern, Date defaultValue){
		if(StringUtil.isEmpty(dateString)){
			return defaultValue;
		}
		try {
			SimpleDateFormat sdf = getDateFormat(pattern);
			Date date = sdf.parse(dateString);
			return date;
		} catch (Exception e) {
			System.err.println("解析日期字符串错误:"+e.getMessage());
		}
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
		if (dateStr.length() == 10) {
			ret = parse(dateStr, DateFormatEnum.YEAR_TO_DAY);
		} else if (dateStr.length() == 16) {
			ret = parse(dateStr, DateFormatEnum.YEAR_TO_MINUTE);
		} else if (dateStr.length() == 19) {
			ret = parse(dateStr, DateFormatEnum.YEAR_TO_SECOND);
		} else if (dateStr.length() == 13) {
			ret = parse(dateStr, DateFormatEnum.YEAR_TO_HOUR);
		} else if (dateStr.length() == 7) {
			ret = parse(dateStr, DateFormatEnum.YEAR_TO_MONTH);
		} else {
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
//	=========================================月份相关日期操作============================================
	
	/**
	 * 
	 * 方法用途: 返回月<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 方法用途: 返回当前月<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static int getMonth() {
		return getMonth(new Date());
	}
//	=======未处理======
	
	/**
	 * 
	 * 方法用途: 获取当前月的第一天日期<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.set(getYear(date), getMonth(date) - 1, 1);
		return c.getTime();
	}
	
	/**
	 * 方法用途: 获取当前月的最后一天日期<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, value);
			return cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前月的最后一天日期<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.set(getYear(date), getMonth(date), 1);
		c.setTimeInMillis(c.getTimeInMillis() - (24 * 3600 * 1000));
		return c.getTime();
	}
	
	/**
	 * 
	 * 方法用途: 返回年<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);

	}
	/**
	 * 
	 * 方法用途: 获得本月的第一天的日期<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String getCurrMonthFirstDay() {
		Calendar cal = Calendar.getInstance();
		String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-01";
		return s;
	}
	/**
	 * 方法用途: 当前年份
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static int getYear(){
		GregorianCalendar gc = new GregorianCalendar();
		return gc.get(GregorianCalendar.YEAR);
	}
	/**
	 * 
	 * 方法用途: 获得本月的最后一天的日期 <br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static String getCurrMonthLastDay() {
		Calendar cal = Calendar.getInstance();
		String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-" + getDays(cal);
		return s;
	}
	/**
	 * 
	 * 方法用途: 获得给定日历的年<br>
	 * 操作步骤: TODO<br>
	 * @param cal
	 * @return
	 */
	public static int getYear(Calendar cal) {
		return cal.get(Calendar.YEAR);
	}
	/**
	 * 
	 * 方法用途: 获得给定日期当月的天数<br>
	 * 操作步骤: TODO<br>
	 * @param cal
	 * @return
	 */
	public static int getDays(Calendar cal) {
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 
	 * 方法用途: 获得给定日历的月<br>
	 * 操作步骤: TODO<br>
	 * @param cal
	 * @return
	 */
	public static int getMonth(Calendar cal) {
		return (cal.get(Calendar.MONTH) + 1);
	}
	/**
	 * 方法用途: 创建一个"yyyyMM"日期的格式化对象
	 * 操作步骤: TODO<br>
	 * @return "yyyyMM"日期的格式化对象
	 */
	private static SimpleDateFormat newShortYMFormat() {
		return new SimpleDateFormat("yyyyMM");
	}
	/**
	 * 方法用途: 获得距离输入月份的diffMonth月的日期
	 * 操作步骤: TODO<br>
	 * @param month "yyyyMM"格式的日期
	 * @param diffMonth 相差的月数
	 * @return "yyyyMM"格式的日期
	 * @throws ParseException
	 */
	public static String getShortYMDiffMonth(String month, int diffMonth) {
		SimpleDateFormat sdf = newShortYMFormat();
		try {
			sdf.parse(month);
		} catch (ParseException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = sdf.getCalendar();
		c.add(Calendar.MONTH, diffMonth);
		return sdf.format(c.getTime());
	}
	
	/**
	 * 方法用途: 获取某月份的最后一天
	 * 操作步骤: TODO<br>
	 * @param shortYM 月份
	 * @return 输入月份的最后一天
	 * @throws Exception
	 */
	public static String getEndDayOfMonth(String shortYM) {
		String month = "";
		try {
			month = getShortYMDiffMonth(shortYM, 1);
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return getShortYMDDiffDay(month + "01", -1);
	}
	/**
	 * 方法用途: 创建一个"yyyyMMdd"日期的格式化对象
	 * 操作步骤: TODO<br>
	 * @return "yyyyMMdd"日期的格式化对象
	 */
	private static SimpleDateFormat newShortYMDFormat() {
		return new SimpleDateFormat("yyyyMMdd");
	}
	/**
	 * 方法用途: 获得距离给定日期diffDay天的日期
	 * 操作步骤: TODO<br>
	 * @param shortYMD "yyyyMMdd"格式的日期
	 * @param diffDay 相差的天数
	 * @return "yyyyMMdd"格式的日期
	 * @throws ParseException
	 */
	public static String getShortYMDDiffDay(String shortYMD, int diffDay) {
		SimpleDateFormat sdf = newShortYMDFormat();
		try {
			sdf.parse(shortYMD);
		} catch (ParseException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = sdf.getCalendar();
		c.add(Calendar.DATE, diffDay);
		return sdf.format(c.getTime());
	}
	/**
	 * 方法用途: 获取可距离某段时间内月份字符串的方法：getAvailableMonth() 使用说明： getAvailableMonth( -1,-5) 如果当前是200205的话，那么返回的月份就是200101到200204，包括当前月份 getAvailableMonth( 3,7)
	 * 如果当前是200202的话，那么返回的月份就是200205到200209，包括当前月份 注意，开始月份永远是靠近当前月份的。
	 * 操作步骤: TODO<br>
	 * @param begin
	 *            开始月份(距离当前月份)
	 * @param end
	 *            结束月份(距离当前月份)
	 * @return string[] 月份数组，格式为200011
	 */
	public static String[] getAvailableMonth(int begin, int end) {
		// 需取得月份的个数
		int numberOfMonth = end - begin;
		// 月份数的绝对值
		int index = Math.abs(numberOfMonth) + 1;
		// 返回的月份数组
		String[] strMonth = new String[index];
		// 回滚年数
		int roll = (end > begin) ? (begin - 1) : (end - 1);
		try {
			for (int i = 1; i <= index; i++) {
				// 依次回滚，计算当前回滚后的月份
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, roll + i);
				Date now = calendar.getTime();
				// 月份格式YYYYMM
				strMonth[i - 1] = getDateString(now, "yyyyMM");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strMonth;
	}
	
	/**
	 * 方法用途: 获得格式为yyyy-MM-dd的格式化日期为字符串
	 * 操作步骤: TODO<br>
	 * @param date
	 *            被格式化日期
	 * @param formatPattern
	 *            格式化格式
	 * @return String 格式化后的日期字符串
	 */
	public static String getDateString(Date date) {
		return getDateString(date, null);
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	
	/**
	 * 方法用途: 根据参数格式化日期为字符串，默认格式为yyyy-MM-dd<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 *            被格式化日期
	 * @param formatPattern
	 *            格式化格式
	 * @return String 格式化后的日期字符串
	 */
	public static String getDateString(Date date, String formatPattern) {
		if (date == null) {
			return "";
		}
		if ((formatPattern == null) || formatPattern.equals("")) {
			formatPattern = "yyyy-MM-dd";
		}
		sdf.applyPattern(formatPattern);
		return sdf.format(date);
	}
	
    /**
     * 方法用途: 返回一个月的第一天<br>
     * 操作步骤: TODO<br>
     * @param year
     * @param month
     * @return
     */
    public static Calendar getFirstDateOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c;
    }

    /**
     * 方法用途: 返回一个月的最后一天<br>
     * 操作步骤: TODO<br>
     * @param year
     * @param month
     * @return
     */
    public static Calendar getLastDateOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        int maxDate = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, maxDate);
        return c;
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
	 * 方法用途: 根据年和月计算出所给的月份最后一天<br>
	 * 操作步骤: TODO<br>
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static int getEndday(int year,int month){
		int endDay=0;
		if(month<1||month>12||year<1753||year>9999)
			return 0;
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
	 * 方法用途: 返回上个月1号和本月1号的格式化的日期对<br>
	 * 操作步骤: TODO<br>
	 * @param formater 日期格式："yyyy-M-d"或"yyyy-MM-dd"
	 * @return
	 */
	public static String[] getDatesRangeOfLastMonth(String formater) {
		String DateRang[] = new String[2];
		SimpleDateFormat format = new SimpleDateFormat(formater);
		Date myDate = new Date();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(myDate);
		calendar.set(calendar.get(GregorianCalendar.YEAR), calendar
				.get(GregorianCalendar.MONTH), 1);
		Date cDate = calendar.getTime();
		DateRang[1] = format.format(cDate);
		calendar.add(GregorianCalendar.MONTH, -1);
		cDate = calendar.getTime();
		DateRang[0] = format.format(cDate);
		return DateRang;
	}
}
