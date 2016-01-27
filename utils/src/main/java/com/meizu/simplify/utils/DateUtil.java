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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class DateUtil {
	
	/**
	 * 
	 * 方法用途: 格式化日期-Date转为指定格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DateFormatEnum.YEAR_TO_MILLISECOND);
	}

	/**
	 * 
	 * 方法用途: 格式化日期-Date转为指定格式字符串<br>
	 * 操作步骤: TODO<br>
	 * @param date
	 * @param type
	 * @return
	 */
	public static String formatDate(Date date, DateFormatEnum type) {
		if (date == null) {
			return null;
		}
		return getDateFormat(type).format(date);
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
            resultString = dateString.replace("/", DateFormatEnum.D.delimiter);
        }
        if (dateString.indexOf("-") > -1) {
            resultString = dateString.replace("/", DateFormatEnum.D.delimiter);
        }
        return resultString;
    }
    
   
}
