package vip.simplify.utils.log.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import vip.simplify.utils.ClassPathUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.utils.log.formatter.DefaultSimpleFormatter;

public class DefaultLogManager{
	private static Logger logger = Logger.getLogger(DefaultLogManager.class.getName());
    private DefaultLogManager(){}
	
    static{
        LogManager.getLogManager().reset(); 
//        resetFromPropertyFile("/properties/logger.properties");
        resetFromPropertyFile("");
	}
	
	public static void resetFromPropertyFile(String filePath){
		if(StringUtil.isBlank(filePath)) {
			return;
		}
        InputStream is  = DefaultLogManager.class.getClass().getResourceAsStream(filePath);
        try {
        	LogManager logManager = LogManager.getLogManager();  
        	logManager.readConfiguration(is);
        } catch (NullPointerException e) {
        	System.err.println("jdk日志配置文件不存在:["+filePath +"] \n"+ e.toString());
        } catch (Exception e) {
        	System.err.println("读取jdk日志配置文件错误：\n" + e.toString());
        } finally{
            try {
            	if(is != null) {
            		is.close();
            	}
            } catch (IOException e) {
            	System.err.println("关闭jdk日志配置文件错误：\n" + e.toString());
            }
        }    
	}

	public static Logger getLogger(){
		
		logger.setLevel(Level.ALL);
		FileHandler fileHandler=null;
		try {
			fileHandler = new FileHandler(ClassPathUtil.getClassPath().substring(1)+"jdklog.log",true);
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(new DefaultSimpleFormatter());
//				fileHandler.setOutputStream(System.out);
			logger.addHandler(fileHandler);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logger;
	}
}
