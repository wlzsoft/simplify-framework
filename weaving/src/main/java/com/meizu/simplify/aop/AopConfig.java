package com.meizu.simplify.aop;
 
import java.io.File;
import java.util.Properties;

import com.meizu.simplify.utils.PropertieUtil;
 
/**
 * <p><b>Title:</b><i>aop javaagent专用配置信息加载</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午5:37:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午5:37:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AopConfig {
    
    private static PropertieUtil propertieUtils= null;
     static {
    	Properties  prop = System.getProperties();
        String config = prop.getProperty("aop.properties");
        if(config == null) {
        	config = "E:/workspace-new/simplify-framework/weaving/src/main/resources/aop.properties";
        }
        propertieUtils = new PropertieUtil(new File(config));
    }
	public static PropertieUtil getUtil() {
		return propertieUtils;
	}
 
}