package com.meizu.aop;
 
import java.io.File;
import java.util.Properties;

import com.meizu.simplify.utils.PropertieUtil;
 
public class Config {
    
    private static PropertieUtil util= null;
     static {
    	Properties  prop = System.getProperties();
        String config = prop.getProperty("aop.properties");
        if(config == null) {
        	config = "E:/workspace-new/simplify-framework/aop/src/main/resources/aop.properties";
        }
        util = new PropertieUtil(new File(config));
    }
	public static PropertieUtil getUtil() {
		return util;
	}
 
}