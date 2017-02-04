package com.meizu.simplify.utils.enums;


/**
 * <p><b>Title:</b><i>日期格式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月4日 下午2:28:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月4日 下午2:28:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public enum DateFormatEnum {
	
//	第一种风格：默认以空格Limiter.DATE_TIME_LIMITER分隔日期和时间，不带时区信息
	
	YEAR("yyyy"),
	/**
	 * yyyy-MM
	 */
	YEAR_TO_MONTH("yyyy" + Limiter.DATE_LIMITER + "MM"),
	/**
	 * yyyy-MM-dd
	 */
	YEAR_TO_DAY("yyyy" + Limiter.DATE_LIMITER + "MM" + Limiter.DATE_LIMITER + "dd"),
	/**
	 * yyyy-MM-dd HH
	 */
	YEAR_TO_HOUR("yyyy" + Limiter.DATE_LIMITER + "MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH"),
	/**
	 * yyyy-MM-dd HH:mm
	 */
	YEAR_TO_MINUTE("yyyy" + Limiter.DATE_LIMITER + "MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH"+Limiter.TIME_LIMITER+"mm"),
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	YEAR_TO_SECOND("yyyy" + Limiter.DATE_LIMITER + "MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss"),
	/**
	 * 全日期:默认格式yyyy-MM-dd HH:mm:ss.SSS
	 */
	YEAR_TO_MILLISECOND("yyyy" + Limiter.DATE_LIMITER + "MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss.SSS"),

	MONTH("MM"),
	MONTH_TO_DAY("MM" + Limiter.DATE_LIMITER + "dd"),
	MONTH_TO_HOUR("MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH"),
	MONTH_TO_MINUTE("MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH:mm"),
	MONTH_TO_SECOND("MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss"),
	MONTH_TO_MILLISECOND("MM" + Limiter.DATE_LIMITER + "dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss.SSS"),

	DAY("dd"),
	DAY_TO_HOUR("dd"+Limiter.DATE_TIME_LIMITER+"HH"),
	DAY_TO_MINUTE("dd"+Limiter.DATE_TIME_LIMITER+"HH:mm"),
	DAY_TO_SECOND("dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss"),
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	DAY_TO_MILLISECOND("dd"+Limiter.DATE_TIME_LIMITER+"HH:mm:ss.SSS"),

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
	MILLISECOND("SSS"),
	
//	第二种风格
	
	/**
	 * 格式yyyyMMddHHmm
	 */
	YEAR_TO_MINUTE_N("yyyyMMddHHmm"),
	/**
	 * 格式yyyyMMddHHmmss
	 */
	YEAR_TO_SECOND_N("yyyyMMddHHmmss"),
	/**
	 * 全日期:默认格式yyyyMMddHHmmssSSSS :TODO 是否特定情况下，会有4个SSSS
	 */
	YEAR_TO_MILLISECOND_N("yyyyMMddHHmmssSSSS");
	
	
	// 以T分隔日期和时间，并带时区信息，符合ISO8601规范，TODO 暂未启用
	/*PATTERN_ISO("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"),
	PATTERN_ISO_ON_SECOND("yyyy-MM-dd'T'HH:mm:ssZZ"),
	PATTERN_ISO_ON_DATE("yyyy-MM-dd")*/;
	
	
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
	 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
	 * @version Version 0.1
	 *
	 */
	public enum Limiter {
		
		/**
		 * 日期分割符
		 */
		DATE_LIMITER("-"),
		/**
		 * 时间分割符
		 */
		TIME_LIMITER(":"),
		/**
		 * 日期时间分隔符
		 */
		DATE_TIME_LIMITER(" ");
		private String value;
		Limiter(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return value;
		}
	}
}
