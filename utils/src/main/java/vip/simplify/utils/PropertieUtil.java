package vip.simplify.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import vip.simplify.exception.StartupErrorException;


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
	
	private Properties props = new Properties();
	private InputStream inputStream = null;

	/**
	 * 构造方法：默认如果文件不存在，会抛致命异常，服务无法启动
	 * @param fileName
	 */
	public PropertieUtil(String fileName) {
		this(fileName,true);
	}
	/**
	 * 构建方法：构建一个配置文件对象，如果文件不存在，可以指定是否抛出异常
	 * @param fileName
	 * @param isError 文件不存在是否抛致命异常
	 */
	public PropertieUtil(String fileName,boolean isError) {
		try {
			inputStream = PropertieUtil.class.getClassLoader().getResourceAsStream(fileName);
			if(inputStream == null) {
				String message = "配置文件["+fileName+"]不存在";
				System.err.println(message);
				if(isError) {
					throw new StartupErrorException(message);
				}
			} else {
				props.load(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public PropertieUtil(File file) {
			try {
				inputStream = new FileInputStream(file);
				props.load(inputStream);
			} catch (FileNotFoundException e) {
//				LOGGER.info("配置文件["+file.getName()+"]不存在");
				System.err.println("配置文件["+file.getName()+"]不存在");
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
			inputStream.close();
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
	 * 操作步骤: 注意：这个方法只能用于启动时调用<br>
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
//			LOGGER.error("配置实体:"+clazz.getName()+"初始化失败");
			System.err.println("配置实体:"+clazz.getName()+"初始化失败");
		}
		return null;
	}
	/**
	 * 
	 * 方法用途: 转换属性文件为pojo对象<br>
	 * 操作步骤: 注意：这个方法只能用于启用时调用<br>
	 * @param obj 待设置配置文件中值的对象
	 * @param prefix 配置文件中属性的前缀
	 * @return
	 */
	public <T extends Object> T setConfigValue(T obj,String prefix){
		if(StringUtil.isNotBlank(prefix)) {
			prefix += ".";
		}
		Field[] fieldArr = obj.getClass().getDeclaredFields();
		for (int i=0; i < fieldArr.length; i++) {
			Field field = fieldArr[i];
			Object value = get(prefix+field.getName());
			if(value == null) {
				continue;
			}
			Class<?> valueClass = field.getType();
			value = DataUtil.convertType(valueClass, value, false);
			ReflectionUtil.invokeSetterMethod(obj, field.getName(), value,valueClass);
		}
		return (T) obj;
	}
	
	/**
	 * 
	 * 方法用途: 获取配置文件内容<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public String get() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<Object, Object>> set = props.entrySet();
		for (Entry<Object, Object> entry : set) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "[" + props +"]";
	}
}
