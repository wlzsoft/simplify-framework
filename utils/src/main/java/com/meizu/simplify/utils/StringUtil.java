package com.meizu.simplify.utils;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class StringUtil {

	
	/**
	 * 方法用途: 判断指定字符串是否为空字符串，不去掉空白字符<br>
	 * 操作步骤: 判断是否为空指针或是长度为0的字符串<br>
	 * @param str 待判断的字符串
	 * @return 
	 */
	public static Boolean isEmpty(String str) {
		if(ObjectUtil.isNull(str)) {
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
	 * 方法用途: 不定长参数,其中所有参数为null或空字符串，则返回true,否则返回false<br>
	 * 操作步骤: TODO<br>
	 * @param str 一个或多个待判断的字符串
	 * @return 
	 */
	public static boolean isEmpty(String... str) {
		for (int i=0; i < str.length; i++) {
			String s = str[i];
			if (StringUtil.isNotEmpty(s)) {
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
		for (int i=0; i < str.length; i++) {
			String s = str[i];
			if (StringUtil.isNotBlank(s)) {
				return false;
			}
		}
		return true;
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
	 * 
	 * 方法用途: 转换字符串第一个字母为小写<br>
	 * 操作步骤: TODO<br>
	 * @param str 待处理字符串
	 * @return 返回转换后的字符串
	 */
	public static String lowerCaseByFirst(String str) {
		if(isBlank(str)) {
			throw new UncheckedException("字符串为空，无法转换为第一个字母为小写");
		}
		str = str.trim();
		String upperChar = str.substring(0,1).toLowerCase();
		if(str.length()>1) {
			str = upperChar+str.substring(1);
		} else {
			str = upperChar;
		}
		return str;
	}
	
	/**
	 * 
	 * 方法用途: 截取最后一个分隔符前的字符串内容<br>
	 * 操作步骤: TODO<br>
	 * @param str 待截取的字符串
	 * @param separator 分隔符
	 * @return 返回最后一个分隔符前的字符串内容
	 */
	public static String substringBeforeLast(String str, String separator) {
		AssertUtil.notNull(str);
		AssertUtil.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 
	 * 方法用途: 截取指定分隔符后的字符串内容<br>
	 * 操作步骤: TODO<br>
	 * @param str 待截取的字符串
	 * @param separator 分隔符
	 * @return 返回指定分隔符后的字符串内容
	 */
	public static String substringAfter(String str, String separator) {
		AssertUtil.notEmpty(str);
		AssertUtil.notEmpty(separator);

		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 
	 * 方法用途: 截取最后一个分隔符后的字符串内容<br>
	 * 操作步骤: TODO<br>
	 * @param str 待截取的字符串
	 * @param separator 分隔符
	 * @return 返回最后一个分隔符后的字符串内容
	 */
	public static String substringAfterLast(String str, String separator) {
		AssertUtil.notEmpty(str);
		AssertUtil.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == (str.length() - separator.length())) {
			return "";
		}
		return str.substring(pos + separator.length());
	}
	/**
	 * 
	 * 方法用途: 根据正则表达式匹配类替换内容<br>
	 * 操作步骤: TODO<br>
	 * @param fileContent 内容
	 * @param patternString 匹配的正则表达式
	 * @param replace 替换的内容
	 * @return
	 */
	public static String replaceAll(String fileContent, String patternString,
			String replace) {
		String str = "";
		Matcher m = null;
		Pattern p = null;
		try {
			p = Pattern.compile(patternString);
			m = p.matcher(fileContent);
			str = m.replaceAll(replace);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			m = null;
			p = null;
		}
		// 获得匹配器对象
		return str;

	}
	
	
	/**
	 * 
	 * 方法用途: 清除所有html标签,保留标签中包裹的内容<br>
	 * 操作步骤: TODO<br>
	 * @param content
	 * @return
	 */
	public static String removeHtmlLabel(String content) {
		if (null == content) return "";
		java.util.regex.Pattern patternHtml;
		java.util.regex.Matcher matcherHtml;
		try {
			patternHtml = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
			matcherHtml = patternHtml.matcher(content);
			content = matcherHtml.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 
	 * 方法用途: 删除所有iframe，清除所有iframe<br>
	 * 操作步骤: TODO<br>
	 * @param content
	 * @return
	 */
	public static String removeIframe(String content) {
		if (null == content) return "";
		java.util.regex.Pattern patternHtml;
		java.util.regex.Matcher matcherHtml;
		try {
			patternHtml = Pattern.compile("<iframe[^>]+>", Pattern.CASE_INSENSITIVE);
			matcherHtml = patternHtml.matcher(content);
			content = matcherHtml.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 
	 * 方法用途: 删除样式，过滤掉样式内容<br>
	 * 操作步骤: TODO<br>
	 * @param content
	 * @return
	 */
	public static String removeStyle(String content) {
		if (null == content) return "";
		java.util.regex.Pattern patternHtml;
		java.util.regex.Matcher matcherHtml;
		try {
			patternHtml = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);
			matcherHtml = patternHtml.matcher(content);
			content = matcherHtml.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 
	 * 方法用途: 删除脚本，过滤掉脚本内容<br>
	 * 操作步骤: TODO<br>
	 * @param content
	 * @return
	 */
	public static String removeScript(String content) {
		if (null == content) return "";
		java.util.regex.Pattern patternHtml;
		java.util.regex.Matcher matcherHtml;
		try {
			patternHtml = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);
			matcherHtml = patternHtml.matcher(content);
			content = matcherHtml.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 
	 * 方法用途: 去除html页面中的所有空格<br>
	 * 操作步骤: 注意，不是普通空格，而是html标准中，可以被浏览器解析的空格标记，主要是\r\n和\s标记
	 *           另外：如果普通标签内有\r\n标记，会出现bug ，比如 <input name=""> 这个标签，input和name之间是\r\n标签<br>
	 * @param content
	 * @return
	 */
	public static String removeHtmlSpace(String content) {
		if (null == content) {
			return "";
		}
		//bugfix： 考虑把比如 <input name=""> 这个标签，input和name之间是\r\n标签 需要转换成一个空格，而不是删除他
		//fixed by lcy : 通过调整第一个正则表达式替换的内容从""，改成" "
		return content
				.replaceAll("\\s*(\\r\\n)\\s*", " ")
				.replaceAll(">(\\s+)", ">")
				.replaceAll("(\\s+)<", "<");
	}

	/**
	 * 
	 * 方法用途: 去除两边空格（包括全角空格）, null 值以空字符代替<br>
	 * 操作步骤: TODO<br>
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (s == null) {
			return "";
		} else {
			return s.trim();
		}
	}

	/**
	 * 
	 * 方法用途: 去除右边的空格<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
	public static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; --i) {
			if (str.charAt(i) != ' ') {
				break;
			}
			--length;
		}
		return str.substring(0, length);
	}

	/**
	 * 
	 * 方法用途: 去除左边的空格<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
	public static String leftTrim(String str) {
		if (str == null) {
			return "";
		}
		int start = 0;
		int i = 0;
		for (int n = str.length(); i < n; ++i) {
			if (str.charAt(i) != ' ') {
				break;
			}
			++start;
		}
		return str.substring(start);
	}
	
	/**
	 * 
	 * 方法用途: 字节数组转换成16进制字符串<br>
	 * 操作步骤: TODO<br>
	 * @param bytes 字节数组
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < bytes.length; i++) {
			byte b = bytes[i];
			int r = b & 0xFF;
			if (r <= 0xF) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(r));
		}
		return sb.toString();
	}

	/**
	 * 
	 * 方法用途: 格式化字符串<br>
	 * 操作步骤: 在str指定占位符 ，以args数组的值填充占位符的值，从左到右<br>
	 * @param str 格式  "this is {0} ,good {1}" 或是 "this is {0} ,good {1}"
	 * @param args 替换用数组数据
	 * @return
	 */
	public static String format(String str, Object... args) {
		AssertUtil.notBlank("字符串不能为空");
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{(\\d+)\\}");
		java.util.regex.Matcher m = p.matcher(str);
		while (m.find()) {
			int index = DataUtil.parseInt(m.group(1));
			if (index < args.length) {
				str = str.replace(m.group(), parseString(args[index], "").toString());
			}
		}
		return str;
	}
	/**
	 * 
	 * 方法用途: 解析字符串，异常或为空返回默认值<br>
	 * 操作步骤: TODO<br>
	 * @param obj 判断对象
	 * @param rep 替换值
	 * @return
	 */
	public static String parseString(Object obj,String rep) {
		if(ObjectUtil.isNull(obj)) {
			return rep;
		} 
		String res = null;
		try {
			res = String.valueOf(obj);
		} catch(Exception e) {
			return rep;
		}
		return res;
	}
	
	
	/**
	 * 
	 * 方法用途: 字符编码转换，默认ISO-8859-1转换成utf-8<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
	public static String coding(String str) {
		return coding(str, "ISO-8859-1");
	}
	
	/**
	 * 
	 * 方法用途: 字符编码转换，默认charset转换成utf-8<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @param charset 待转换字符编码
	 * @return
	 */
	public static String coding(String str, String charset) {
		return coding(str, charset, "UTF-8");
	}
	
	/**
	 * 
	 * 方法用途: 字符编码转换<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @param charset 待转换字符编码
	 * @param tocharset 目标字符编码
	 * @return
	 */
	public static String coding(String str, String charset, String tocharset) {
		try {
			return str == null ? "" : new String(str.getBytes(charset), tocharset);
		} catch (Exception E) {
			return str;
		}
	}
	
	
	/**
	 * 
	 * 方法用途: 过滤掉所有的html字符，包含script，style,Iframe,html空格字符<br>
	 * 操作步骤: TODO 已测试，待验证可用性<br>
	 * @param content
	 * @return
	 */
	public static String clearHTML(String content) {
		if (null == content) return "";
		content = removeScript(content);
		content = removeStyle(content);
		content = removeIframe(content);
		content = removeHtmlSpace(content);
		content = removeHtmlLabel(content);
		return content;
	}

	/**
	 * 
	 * 方法用途: 集合中的各个元素转换成字符串，并以指定字符隔开，拼接成一个长字符串<br>
	 * 操作步骤: TODO<br>
	 * @param collection
	 * @param separator
	 * @return
	 */
	public static String join(Collection<?> collection, String separator) {
		if(collection == null) {
			return null;
		}
		return join(collection.toArray(),separator);
	}
	
	/**
	 * 
	 * 方法用途: 集合中的各个元素转换成字符串，并以指定字符隔开，拼接成一个长字符串<br>
	 * 操作步骤: TODO:还未实现，待实现<br>
	 * @param collection
	 * @param separator
	 * @return
	 */
	public static String join(Object[] arr, String separator) {
		if(arr == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < arr.length; i++) {
			Object obj = arr[i];
			sb.append(separator).append(obj);
		}
		return sb.toString().substring(1);
	}
}
