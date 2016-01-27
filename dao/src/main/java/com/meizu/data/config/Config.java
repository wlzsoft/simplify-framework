package com.meizu.data.config;

import java.util.Properties;


import com.meizu.simplify.ioc.annotation.Bean;

/**
 * <p><b>Title:</b><i>读取配置文件信息</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午2:11:50</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午2:11:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Bean
public class Config extends Properties{
	
	private static final long serialVersionUID = -8967067041497686633L;
//	@Value("${unicodeTranscoding}")
	public boolean unicodeTranscoding;
	public static boolean cache = false;
	public static String directory_provider = ""; // 加载器
    public static Integer limitExecutionTime = 0; // 默认关闭
	
	/*
	 * 测试开关，true表示以测试方式启动项目 测试方式启动项目则缓存方式无效
	 */
	public static boolean test_switch = false;
	
	public static boolean debug = false;
	public static String charSet = null;
	public static String webcharSet = "ISO-8859-1";
	public static Integer urlcacheCount = 100;
	public static String class_path; // class位置
	public static String directives; // velocity自定义Directive
	
}
