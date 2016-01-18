package com.meizu.aop;
 
import java.util.Properties;

import com.meizu.simplify.utils.PropertieUtil;
 
public class Config {
    
    private static PropertieUtil util= null;
    public Config() {
    	Properties  prop = System.getProperties();
        String config = prop.getProperty("aop.properties");
        if(config == null) {
        	config = "E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties";
        }
        util = new PropertieUtil(config);
    }
	public static PropertieUtil getUtil() {
		return util;
	}
 
}