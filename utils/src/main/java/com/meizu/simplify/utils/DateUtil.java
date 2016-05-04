package com.meizu.simplify.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	 * 
	 * 方法用途: 格式化日期-Date转为[yyyy-MM-dd HH:mm:ss.SSS]格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, DateFormatEnum.YEAR_TO_MILLISECOND);
	}
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为指定格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date  java.util.Date格式
	 * @param type yyyy-MM-dd HH:mm:ss | yyyy年MM月dd日 HH时mm分ss秒
	 * @return
	 */
	public static String format(Date date, DateFormatEnum type) {
		if (date == null) {
			return null;
		}
		try {
			return getDateFormat(type).format(date);
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
	 * @param type
	 * @return
	 * @author luchuangye 2016/4/7
	 */
	public static String format(long timesamp, DateFormatEnum type) {
		return format(new Date(timesamp));
	}
	
	
    /**
     * 
     * 方法用途: 获取SimpleDateFormat对象<br>
     * 操作步骤: TODO<br>
     * @param type
     * @return
     */
    private static SimpleDateFormat getDateFormat(DateFormatEnum type) {
        String pattern = type.value();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        // TimeZone zone = new SimpleTimeZone(28800000, "Asia/Shanghai");
        // formatter.setTimeZone(zone);
        return formatter;
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
	 * @param str 字符串形式的日期
	 * @param pattern 格式化格式
	 * @return 根据字符串格式化后的日期
	 */
	public static Date parse(String str, DateFormatEnum pattern) {
		return parse(str,pattern,null);
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
}
