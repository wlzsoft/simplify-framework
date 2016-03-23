package com.meizu.simplify.config;

import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月23日 上午11:10:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月23日 上午11:10:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class PropertiesConfig {

	private static final String PROPERTIESFILE = "properties/config.properties";
	private static final PropertieUtil propertieUtils = new PropertieUtil(PROPERTIESFILE);

	public PropertieUtil getProperties() {
		return propertieUtils;
	}
	
	/**
	 * 方法用途: 根据通配符资源路径获取资源路径列表<br>
	 * 操作步骤: TODO<br>
	 * @param resourceDir  资源目录
	 * @param wildcardResPaths   通配符资源路径
	 * @return 返回实际匹配的资源路径列表
	 */
	public static List<String> getResList(String resourceDir,
			String... wildcardResPaths) {
		List<String> resourcePaths = new ArrayList<String>();
		try {
			for (Res resource : getRessByWildcard(wildcardResPaths)) {
				String uri = resource.getURI().toString();
				String resourcePath = "classpath:" + resourceDir+ StringUtil.substringAfter(uri, resourceDir);
				resourcePaths.add(StringUtil.substringBeforeLast(resourcePath,"."));
			}
		} catch (Exception e) {
			throw new UncheckedException("获取资源文件时发生异常。", e);
		}
		return resourcePaths;
	}

	private static List<Res> getRessByWildcard(String[] wildcardResPaths) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Boolean debug = true;
	// 后续使用枚举类型 TODO
	private String charset = "UTF-8";
	private String classpath = "com.meizu.demo.mvc.controller";
	private String template = "meizu";
	// velocity自定义Directive
	private String directives = "";//暂未启用
	private Integer urlcacheCount = 100;//暂未启用

	private Boolean unicodeTranscoding = true;//暂未启用
	private Boolean cache = false;//暂未启用
	// lucence加载器
	private String directory_provider = "";//暂未启用
	// lucence配置 默认关闭
	private Integer limitExecutionTime = 0;//暂未启用
	// 页面级别的乱码控制，主要是post和get请求可能会产生的乱码问题，目前暂未开放 TODO
	private String webcharSet = "ISO-8859-1";//暂未启用
	
	//页面文件缓存路径配置
	private String fileCachePath = "D:/htmlCache/";

	public PropertiesConfig() {
		debug = propertieUtils.getBoolean("system.debug", false);
		charset = propertieUtils.getString("system.charset", null);
		classpath = propertieUtils.getString("system.classpath", null);
		directives = propertieUtils.getString("system.directives", null);
		webcharSet = propertieUtils.getString("system.webcharSet", "ISO-8859-1");
	}
	
	public Boolean getDebug() {
		return debug;
	}
	public void setDebug(Boolean debug) {
		this.debug = debug;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getClasspath() {
		return classpath;
	}
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getDirectives() {
		return directives;
	}
	public void setDirectives(String directives) {
		this.directives = directives;
	}
	public Integer getUrlcacheCount() {
		return urlcacheCount;
	}
	public void setUrlcacheCount(Integer urlcacheCount) {
		this.urlcacheCount = urlcacheCount;
	}
	public Boolean getUnicodeTranscoding() {
		return unicodeTranscoding;
	}
	public void setUnicodeTranscoding(Boolean unicodeTranscoding) {
		this.unicodeTranscoding = unicodeTranscoding;
	}
	public Boolean getCache() {
		return cache;
	}
	public void setCache(Boolean cache) {
		this.cache = cache;
	}
	public String getDirectory_provider() {
		return directory_provider;
	}
	public void setDirectory_provider(String directory_provider) {
		this.directory_provider = directory_provider;
	}
	public Integer getLimitExecutionTime() {
		return limitExecutionTime;
	}
	public void setLimitExecutionTime(Integer limitExecutionTime) {
		this.limitExecutionTime = limitExecutionTime;
	}
	public String getWebcharSet() {
		return webcharSet;
	}
	public void setWebcharSet(String webcharSet) {
		this.webcharSet = webcharSet;
	}

	public String getFileCachePath() {
		return fileCachePath;
	}
	
	public void setFileCachePath(String fileCachePath) {
		this.fileCachePath = fileCachePath;
	}
}
