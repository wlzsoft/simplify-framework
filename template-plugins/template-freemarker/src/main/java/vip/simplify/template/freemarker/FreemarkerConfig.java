package vip.simplify.template.freemarker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.annotation.DymaicProperties;
import vip.simplify.config.annotation.Reload;
import vip.simplify.config.annotation.ReloadableResource;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.utils.PropertieUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年6月3日 下午1:31:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年6月3日 下午1:31:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@ReloadableResource(value="properties/freemarker.properties",prefix="freemarker")
public class FreemarkerConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerConfig.class);
	
	private String defaultEncoding = "UTF-8";
	private String locale;// = "zh_CN";
	private String numberFormat = "computer";
	private String booleanFormat = "true,false";
	private String dateFormat = "yyyy-MM-dd";
	private String timeFormat = "HH:mm:ss";
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	private String autoImport;
	private String urlEscapingCharset="UTF-8";
	
	/**
	 * 刷新模板的周期，单位为秒
	 */
	private Integer templateUpdateDelay;// = 5;
	
	
	public String getUrlEscapingCharset() {
		return urlEscapingCharset;
	}

	public void setUrlEscapingCharset(String urlEscapingCharset) {
		this.urlEscapingCharset = urlEscapingCharset;
	}

	public Integer getTemplateUpdateDelay() {
		return templateUpdateDelay;
	}

	public void setTemplateUpdateDelay(Integer templateUpdateDelay) {
		this.templateUpdateDelay = templateUpdateDelay;
	}

	public String getAutoImport() {
		return autoImport;
	}

	public void setAutoImport(String autoImport) {
		this.autoImport = autoImport;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public String getNumberFormat() {
		return numberFormat;
	}
	
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}
	
	public String getBooleanFormat() {
		return booleanFormat;
	}

	public void setBooleanFormat(String booleanFormat) {
		this.booleanFormat = booleanFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
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
		LOGGER.info("加载Freemarker.properties配置信息文件成功。");
	}
}
