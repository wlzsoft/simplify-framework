package com.meizu.simplify.utils;
import com.meizu.simplify.exception.UncheckedException;
/**
 * 
 * <p><b>Title:</b><i>字符串工具</i></p>
 * <p>Desc: 字符串工具</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:32:14</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:32:14</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class StringUtils {

	/**
	 * 方法用途: 是否为空指针<br>
	 * 操作步骤: TODO<br>
	 * @param str 待判断的字符串
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null ? true : false;
	}
	
	/**
	 * 方法用途: 判断指定字符串是否为空字符串，不去掉空白字符<br>
	 * 操作步骤: 判断是否为空指针或是长度为0的字符串<br>
	 * @param str 待判断的字符串
	 * @return 
	 */
	public static Boolean isEmpty(String str) {
		if(isNull(str)) {
			return true;
		}
		return str.isEmpty();
	}
	
	/**
	 * 方法用途: 判断指定字符串是否为空字符串,去掉空白字符<br>
	 * 操作步骤: 判断是否为空指针或是去掉空格后长度为0的字符串<br>
	 * @param str 待判断的字符串
	 * @return 
	 */
	public static Boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}
		if(isEmpty(str.trim())) {
			 return true;	
		}
		return false;
	}
	
	/**
	 * 方法用途: 不定长参数,其中所有参数为null，则返回true,否则返回false<br>
	 * 操作步骤: TODO<br>
	 * @param str 一个或多个待判断的字符串
	 * @return 
	 */
	public static boolean isNull(String... str) {
		for (String s : str) {
			if (StringUtils.isNotNull(s)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 方法用途: 不定长参数,其中所有参数为null或空字符串，则返回true,否则返回false<br>
	 * 操作步骤: TODO<br>
	 * @param str 一个或多个待判断的字符串
	 * @return 
	 */
	public static boolean isEmpty(String... str) {
		for (String s : str) {
			if (StringUtils.isNotEmpty(s)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 方法用途: 不定长参数,其中所有参数为null或空或trim后为空格字符串则返回true,否则返回false<br>
	 * 操作步骤: TODO<br>
	 * @param str 一个或多个待判断的字符串
	 * @return
	 */
	public static boolean isBlank(String... str) {
		for (String s : str) {
			if (StringUtils.isNotBlank(s)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 方法用途: 是否不为空，是为true，否为false<br>
	 * 操作步骤: TODO<br>
	 * @param str 待判断的字符串
	 * @return
	 */
	public static boolean isNotNull(String str) {
		return !isNull(str);
	}
	
	/**
	 * 
	 * 方法用途: 判断指定字符串是否不为空<br>
	 * 操作步骤: TODO<br>
	 * @param str 待判断的字符串
	 * @return 返回指定字符串是否不为空
	 */
	public static Boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	
	/**
	 * 方法用途: 判断字符串非空<br>
	 * 操作步骤: 判断指定字符串是否不为空字符串<br>
	 * @param str 待判断的字符串
	 * @return 返回指定字符串是否不为空字符串。
	 */
	public static Boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	
	/**
	 * 
	 * 方法用途: 转换字符串第一个字母为大写<br>
	 * 操作步骤: TODO<br>
	 * @param str 待处理字符串
	 * @return 返回转换后的字符串
	 */
	public static String capitalize(String str) {
		if(isBlank(str)) {
			throw new UncheckedException("字符串为空，无法转换为第一个字母为大写");
		}
		str = str.trim();
		String upperChar = str.substring(0,1).toUpperCase();
		if(str.length()>1) {
			str = upperChar+str.substring(1);
		} else {
			str = upperChar;
		}
		return str;
	}
	

	

	/**
	 * 截取最后一个分隔符前的字符串内容。
	 * 
	 * @param str
	 *            待截取的字符串
	 * @param separator
	 *            分隔符
	 * @return 返回最后一个分隔符前的字符串内容。
	 */
	public static String substringBeforeLast(String str, String separator) {
		Assert.notNull(str);
		Assert.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取指定分隔符后的字符串内容。
	 * 
	 * @param str
	 *            待截取的字符串
	 * @param separator
	 *            分隔符
	 * @return 返回指定分隔符后的字符串内容。
	 */
	public static String substringAfter(String str, String separator) {
		Assert.notEmpty(str);
		Assert.notEmpty(separator);

		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取最后一个分隔符后的字符串内容。
	 * 
	 * @param str
	 *            待截取的字符串
	 * @param separator
	 *            分隔符
	 * @return 返回最后一个分隔符后的字符串内容。
	 */
	public static String substringAfterLast(String str, String separator) {
		Assert.notEmpty(str);
		Assert.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == (str.length() - separator.length())) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

}
