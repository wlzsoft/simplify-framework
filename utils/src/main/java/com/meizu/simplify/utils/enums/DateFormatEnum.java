package com.meizu.simplify.utils.enums;

/**
 * <p>
 * <b>Title:</b><i>日期格式</i>
 * </p>
 * <p>
 * Desc: TODO
 * </p>
 * <p>
 * source folder:{@docRoot}
 * </p>
 * <p>
 * Copyright:Copyright(c)2014
 * </p>
 * <p>
 * Company:meizu
 * </p>
 * <p>
 * Create Date:2016年1月27日 下午8:21:41
 * </p>
 * <p>
 * Modified By:luchuangye-
 * </p>
 * <p>
 * Modified Date:2016年1月27日 下午8:21:41
 * </p>
 * 
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public enum DateFormatEnum {
	
	
	
	YEAR("yyyy"),
	YEAR_TO_MONTH("yyyy" + D.delimiter + "MM"),
	YEAR_TO_DAY("yyyy" + D.delimiter + "MM" + D.delimiter + "dd"),
	YEAR_TO_HOUR("yyyy" + D.delimiter + "MM" + D.delimiter + "dd HH"),
	YEAR_TO_MINUTE("yyyy" + D.delimiter + "MM" + D.delimiter + "dd HH:mm"),
	YEAR_TO_SECOND("yyyy" + D.delimiter + "MM" + D.delimiter + "dd HH:mm:ss"),
	/**
	 * 全日期:默认格式yyyy-MM-dd HH:mm:ss
	 */
	YEAR_TO_MILLISECOND("yyyy" + D.delimiter + "MM" + D.delimiter + "dd HH:mm:ss.SSS"),

	MONTH("MM"),
	MONTH_TO_DAY("MM" + D.delimiter + "dd"),
	MONTH_TO_HOUR("MM" + D.delimiter + "dd HH"),
	MONTH_TO_MINUTE("MM" + D.delimiter + "dd HH:mm"),
	MONTH_TO_SECOND("MM" + D.delimiter + "dd HH:mm:ss"),
	MONTH_TO_MILLISECOND("MM" + D.delimiter + "dd HH:mm:ss.SSS"),

	DAY("dd"),
	DAY_TO_HOUR("dd HH"),
	DAY_TO_MINUTE("dd HH:mm"),
	DAY_TO_SECOND("dd HH:mm:ss"),
	DAY_TO_MILLISECOND("dd HH:mm:ss.SSS"),

	HOUR("HH"),
	HOUR_TO_MINUTE("HH:mm"),
	HOUR_TO_SECOND("HH:mm:ss"),
	HOUR_TO_MILLISECOND("HH:mm:ss.SSS"),

	MINUTE("mm"),
	MINUTE_TO_SECOND("mm:ss"),
	MINUTE_TO_MILLISECOND("mm:ss.SSS"),

	SECOND("ss"),
	SECOND_TO_MILLISECOND("ss.SSS"),
	/**
	 * 毫秒
	 */
	MILLISECOND("SSS");
	
	
	private String formatStr;

	public String value() {
		return formatStr;
	}

	/**
	 * @param formatStr 日期格式字符串
	 */
	private DateFormatEnum(String formatStr) {
		this.formatStr = formatStr;
	}
	
	@Override
	public String toString() {
		return formatStr;
	}
	
	/**
	 * <p><b>Title:</b><i>日期分隔符</i></p>
	 * <p>Desc: TODO</p>
	 * <p>source folder:{@docRoot}</p>
	 * <p>Copyright:Copyright(c)2014</p>
	 * <p>Company:meizu</p>
	 * <p>Create Date:2016年1月27日 下午8:33:47</p>
	 * <p>Modified By:luchuangye-</p>
	 * <p>Modified Date:2016年1月27日 下午8:33:47</p>
	 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
	 * @version Version 0.1
	 *
	 */
	public class D {
		public static final String delimiter = "-"; // 日期分割符
	}
}
