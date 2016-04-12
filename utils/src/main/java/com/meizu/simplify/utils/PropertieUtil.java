package com.meizu.simplify.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.StartupErrorException;


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
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class PropertieUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertieUtil.class);
	private Properties props = new Properties();
	private InputStream jndiInput = null;

	public PropertieUtil(String fileName) {
		try {
			jndiInput = PropertieUtil.class.getClassLoader().getResourceAsStream(fileName);
			props.load(jndiInput);
		} catch (NullPointerException e) {
			e.printStackTrace();
			String message = "配置文件["+fileName+"]不存在";
//			LOGGER.info(message);
			System.out.println(message);
			throw new StartupErrorException(message);
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
//				LOGGER.info("配置文件["+file.getName()+"]不存在");
				System.out.println("配置文件["+file.getName()+"]不存在");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public Object get(String name) {
		return props.get(name);
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
	 * 方法用途: 获取properties对象<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public Properties getProps() {
		return props;
	}
	/**
	 * 
	 * 方法用途: 转换属性文件为pojo对象-无属性前缀<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 转换的pojo的具体类型
	 * @return
	 */
	public <T> T toClass(Class<T> clazz){
		return toClass(clazz,"");
	}
	/**
	 * 
	 * 方法用途: 转换属性文件为pojo对象-无属性前缀<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 转换的pojo的具体类型
	 * @param prefix 配置文件中属性的前缀
	 * @return
	 */
	public <T> T toClass(Class<T> clazz, String prefix){
		try {
			return setConfigValue(clazz.newInstance(),prefix);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			LOGGER.error("配置实体:"+clazz.getName()+"初始化失败");
		}
		return null;
	}
	/**
	 * 
	 * 方法用途: 转换属性文件为pojo对象<br>
	 * 操作步骤: TODO<br>
	 * @param obj 待设置配置文件中值的对象
	 * @param prefix 配置文件中属性的前缀
	 * @return
	 */
	public <T> T setConfigValue(Object obj,String prefix){
		if(StringUtil.isNotBlank(prefix)) {
			prefix += ".";
		}
		Field[] fieldArr = obj.getClass().getDeclaredFields();
		for (Field field : fieldArr) {
				Object value = get(prefix+field.getName());
   				if(value == null) {
   					continue;
   				}
   				if(field.getType() == Boolean.class||field.getType() == boolean.class) {
   					value = DataUtil.parseBoolean(value);
   				} else if(field.getType() == Integer.class || field.getType() == int.class){
   					value = DataUtil.parseInt(value);
   				}
				ReflectionUtil.invokeSetterMethod(obj, field.getName(), value);
		}
		return (T) obj;
	}
	
	@Override
	public String toString() {
		return "[" + props +"]";
	}
}
