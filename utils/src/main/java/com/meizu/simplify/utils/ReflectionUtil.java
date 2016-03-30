package com.meizu.simplify.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.StartupException;
import com.meizu.simplify.exception.UncheckedException;


/**
 * <p><b>Title:</b><i>反射工具类</i></p>
 * <p>Desc:  反射的Utils函数集合. 提供访问私有变量,获取泛型类型Class,提取集合中元素的属性等Utils函数.
 * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数.</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月25日 下午6:08:10</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月25日 下午6:08:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class ReflectionUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
    
    private ReflectionUtil() {
    }
    
    /**
     * 
     * 方法用途: 调用Get方法<br>
     * 操作步骤: TODO<br>
     * @param obj 对象
     * @param propertyName 属性名
     * @return
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtil.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName);
    }
     
    /**
     * 
     * 方法用途: 调用Set方法<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param propertyName 属性名
     * @param value
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value,Class<?> valueClazz) {
    	String setterMethodName = "set" + StringUtil.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{valueClazz}, new Object[]{value});
    }
    
    /**
     * 
     * 方法用途: 调用Set方法<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param propertyName 属性名
     * @param value
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
    	invokeSetterMethod(obj,propertyName,value,value.getClass());
    }
    
    
    /**
     * 
     * 方法用途: 直接调用对象方法，忽视private/protected修饰符<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeMethod(final Object obj, final String methodName) {
        return invokeMethod(obj, methodName,null,null);
    }
    
    
    /**
     * 
     * 方法用途: 直接调用对象方法，忽视private/protected修饰符<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     */
    public static Object invokeMethod(final Object obj,final  String methodName,final  Class<?>[] parameterTypes,final Object[] args) {
        Method method = obtainAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
        	throw new IllegalArgumentException("不能找到对象 [" + obj + "] 的方法 [" + methodName + "("+parameterTypes.getClass()+")] 请检查方法名和方法调用参数是否指定正确"); 
        }
        try {
        	return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.error("参数类型不匹配！");
            throw new UncheckedException("参数类型不匹配！");//后续再补充提示信息，指明源数据类型和目录属性类型 TODO
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LOGGER.error("反射方法调用异常",e.getTargetException());
            throw new StartupException(e.getTargetException());
        }
        return null;
    }

    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: 
     * 需要重构这个方法：TODO ，fieldName 和 parameterTypes有些冗余
     * 循环向上转型，获取对象的DeclaredMethod,并强制设置为可访问 如向上转型到Object仍无法找到，返回null
     * 
     * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj,Object...
     * args)<br>
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Method obtainAccessibleMethod(final Object obj,  final String methodName, final Class<?>... parameterTypes) {
    	AssertUtil.notNull(obj, "目标调用对象不能为空");
        Class<?> superClass = obj.getClass();
        while(superClass != Object.class) {
            try {
            	Method method = superClass.getDeclaredMethod(methodName,parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
            	superClass = superClass.getSuperclass();
            	continue;// Method不在当前类定义,继续向上转型
            } catch (SecurityException e) {
            	throw new UncheckedException("安全异常");
            }
        }
        return null;
    }
    
    
    
    
    
   
    /**
     * 直接读取对象属性值 忽视private/protected修饰符，不经过getter函数
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static <T> T obtainFieldValue(final Object obj,
            final String fieldName,Class<T> classz) {
    	Field field = getField(obj,fieldName);
        T retval = null;
        try {
//        	if(classz.equals(field.getType())) { //TODO 注意类型处理
        		retval = (T) field.get(obj);
//        	}
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;
         
    }
    
    /**
     * 
     * 方法用途: 直接读取对象属性值 忽视private/protected修饰符，不经过getter函数<br>
     * 操作步骤: 注意：和invokeGetterMethod功能重复，后续确认抛弃掉其中一个方法 TODO<br>
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object obtainFieldValue(final Object obj,
            final String fieldName) {
    	Field field = getField(obj,fieldName);
        Object retval = null;
        try {
            retval = field.get(obj);
//            if(retval == null) {
//            	String type = field.getGenericType().toString();
//            	if(type == "class java.util.Date") {
//            		retval = new Date();
//            	}
//            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;
    }
    
    /**
   	 * 方法用途: 抽取指定对象的指定属性的值<br> 
   	 * 操作步骤: 直接读取对象属性值 忽视private/protected修饰符，不经过getter函数<br>
   	 * @param obj 指定的对象
   	 * @param fieldName 指定的属性（字段）
   	 * @return
   	 */
    public static Field getField(final Object obj,final String fieldName) {
        Field field = obtainAccessibleField(obj, fieldName);
        if (field == null) { throw new IllegalArgumentException(
                "could not find field [" + fieldName + "] on target ["
                        + obj + "]"); }
        return field;
        
         
    }
    
    /**
     * 
     * 方法用途: 循环向上转型，获取对象的DeclaredField,并强制设为可访问 如向上转型Object仍无法找到，返回null<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field obtainAccessibleField(final Object obj, final String fieldName) {
    	AssertUtil.notNull(obj, "object不能为空");
        Class<?> superClass = obj.getClass();
        Class<Object> objClass = Object.class;
        for (; superClass != objClass; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义，向上转型
            } catch (SecurityException e) {
                // Field不在当前类定义，向上转型
            }
        }
       
        return null;
    }
 
    
    
    /**
     * 
     * 方法用途: 设置构造函数为可访问<br>
     * 操作步骤: 可访问后，就可以设置构造函数的值<br>
     * @param constructor
     */
    public static void makeAccessible(Constructor<?> constructor) {
		if ((!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) &&
				!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}
	}
    
    /**
     * 
     * 方法用途: 反射创建对象<br>
     * 操作步骤: 通过指定构造函数来反射创建对象
     * 调用这个方法之前，如构造函数无访问权限，可以通过执行makeAccessible方法获取访问权限<br>
     * @param constructor
     * @param args
     * @return
     */
    public static <T> T instantiateClass(Constructor<T> constructor, Object... args)  {
		try {
			makeAccessible(constructor);
			return constructor.newInstance(args);
		} catch (IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 方法用途: 实体转map<br>
	 * 操作步骤: TODO<br>
	 * @param param
	 * @return
	 */
	public  static <T extends Object> Map<String, Object> bean2Map(T param) {
		Map<String,Object> map = new ConcurrentHashMap<>();
		buildFieldInfo(param.getClass(),param,map);
		return map;
	}
	
	/**
	 * 方法用途: 无论有多少超类，能递归判断和提取<br>
	 * 操作步骤: TODO<br>
	 * @param class1
	 * @param trans 
	 */
	private static <T> void buildFieldInfo(Class<? extends T> class1,T param,Map<String,Object> map) {
		Field[] fields = class1.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			map.put(fieldName, invokeGetterMethod(param, field.getName()));
		}
		if(class1.getSuperclass() != Object.class && class1.getSuperclass() != null) {
			buildFieldInfo(class1.getSuperclass(),param,map);
		}
	}
	
    
}
