package com.meizu.simplify.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * <p><b>Title:</b><i>配置文件工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:42:36</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:42:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class PropertieUtil {
	
	private Logger LOGGER = LoggerFactory.getLogger(PropertieUtil.class);
	private Properties props = new Properties();
	private InputStream jndiInput = null;

	public PropertieUtil(String fileName) {
		try {
			jndiInput = PropertieUtil.class.getClassLoader().getResourceAsStream(fileName);
			props.load(jndiInput);
		} catch (NullPointerException e) {
			e.printStackTrace();
			LOGGER.info("配置文件["+fileName+"]不存在");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public PropertieUtil(File file) {
			try {
				jndiInput = new FileInputStream(file);
				props.load(jndiInput);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.info("配置文件["+file.getName()+"]不存在");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public String getProperty(String name) {
		return props.getProperty(name);
	}
	
	public String getString(String name) {
		return props.getProperty(name);
	}
	
	public String getString(String name, String df) {
		return props.getProperty(name, df);
	}

	public Boolean getBoolean(String name) {
		String val = props.getProperty(name);
		return val == null ? null : Boolean.valueOf(val);
	}
	
	public Boolean getBoolean(String name, Boolean df) {
		String val = props.getProperty(name);
		return val == null ? df : Boolean.valueOf(val);
	}
	
	public Integer getInteger(String name) {
		String val = props.getProperty(name);
		return val == null ? null : Integer.parseInt(val);
	}
	
	public Integer getInteger(String name, Integer df) {
		String val = props.getProperty(name);
		return val == null ? df : DataUtil.parseInt(val);
	}
	
	public Object set(String key, String value) {
		return props.setProperty(key, value);
	}
	
	public void close() {
		try {
			jndiInput.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * 
	 * 方法用途: 获取所有属性名<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Set<String> stringPropertyNames(){
		return props.stringPropertyNames();
	}
	
	/**
	 * 
	 * 方法用途: 获取所有属性<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Set<Entry<Object, Object>> propertys(){
		return props.entrySet();
	}
	
	/**
	 * 
	 * 方法用途: 转换属性文件为pojo对象<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public <T> T toClass(Class<T> clazz){
		try {
			Object obj = clazz.newInstance();
			Method[] methodArr = clazz.getMethods();
			
			for (Method method : methodArr) {
				String methodName = method.getName();
				if(methodName.startsWith("set")) {
					for (String name : stringPropertyNames()) {
						if(methodName.substring(3).equalsIgnoreCase(name)) {
							Object arg= null;
							Class<?> type = null;
							try {
								arg = getInteger(name);
								type = Integer.class;
							} catch(Exception ex) {
								try {
									arg = getBoolean(name);
									type = Boolean.class;
								} catch (Exception e) {
									arg = getString(name);
									type = String.class;
								}
							} 
							ReflectionUtil.invokeMethod(obj, methodName,new Class<?>[]{type},new Object[]{arg});
							break;
						}
					}
				}
			}
			return (T) obj;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
