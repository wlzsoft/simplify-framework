package com.meizu.simplify.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.DymaicProperties;
import com.meizu.simplify.config.annotation.Reload;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.PropertieUtil;


/**
 * <p><b>Title:</b><i>配置信息</i></p>
 * <p>Desc: 支持热加载</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月23日 上午11:10:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月23日 上午11:10:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@ReloadableResource(value="properties/config.properties",prefix="system")
public class PropertiesConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesConfig.class);
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
	private String directoryProvider = "";//暂未启用
	// lucence配置 默认关闭
	private Integer limitExecutionTime = 0;//暂未启用
	// 页面级别的乱码控制，主要是post和get请求可能会产生的乱码问题，目前暂未开放 TODO
	private String webcharSet = "ISO-8859-1";//暂未启用
	
	//页面文件缓存路径配置
	private String fileCachePath = "D:/htmlCache/";

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
	
	public String getDirectoryProvider() {
		return directoryProvider;
	}

	public void setDirectoryProvider(String directoryProvider) {
		this.directoryProvider = directoryProvider;
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
	
	@DymaicProperties
	private PropertieUtil propertieUtil;
	
	public void setPropertieUtil(PropertieUtil propertieUtil) {
		this.propertieUtil = propertieUtil;
	}
	public PropertieUtil getProp() {
		return propertieUtil;
	}
	
	
	
	@Reload
	public void setBasenames(String... basenames) {
		LOGGER.info("加载配置信息文件成功。");
	}
}
