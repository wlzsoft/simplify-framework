package com.meizu.simplify.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class ReflectionUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
    
    private ReflectionUtil() {
    }
    
	/**
	 * 方法用途: 反射方法调用<br>
	 * 操作步骤: TODO<br>
	 * @param obj 调用对象
	 * @param methodName 调用方法名
	 * @param args 调用方法参数，零个到多个
	 */
	public static Object invoke(Object obj, String methodName, Object... args) {
		Class<?>[] parameterTypes = null;
		if (args != null) {
			parameterTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				parameterTypes[i] = args[i].getClass();
			}
		}
		Class<?> clazz = obj.getClass();
		try {
			Method method = clazz.getMethod(methodName, parameterTypes);
			return method.invoke(obj, args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			LOGGER.error("反射获取方法对象失败:类["+clazz.getName()+"]没有["+methodName+"]这个方法,请检查是否方法写错，或是方法参数类型写错"+e.getMessage());
            throw new UncheckedException("反射获取方法对象失败:类["+clazz.getName()+"]没有["+methodName+"]这个方法,请检查是否方法写错，或是方法参数类型写错");
		} catch (SecurityException e) {
			e.printStackTrace();
			LOGGER.error("反射获取方法对象失败:类["+clazz.getName()+"]的方法["+methodName+"]由于java安全限制而无法调用"+e.getMessage());
            throw new UncheckedException("反射获取方法对象失败:类["+clazz.getName()+"]的方法["+methodName+"]由于java安全限制而无法调用");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			LOGGER.error("反射调用方法失败:类["+clazz.getName()+"]的方法["+methodName+"]"+e.getMessage());
            throw new UncheckedException("反射调用方法失败:类["+clazz.getName()+"]的方法["+methodName+"]");
		}
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
//    	TODO 这里不考虑boolean的is方法的处理
//    	if (type.getName().equals("boolean")) {
//      String getterMethodName = "is" + StringUtil.capitalize(propertyName);
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
        	String parameterTypeClsName = "";
        	if(parameterTypes != null && parameterTypes.length > 0){
        		parameterTypeClsName = "(" + parameterTypes[0].getName() + ")";
        	}
        	throw new IllegalArgumentException("不能找到对象 [" + obj + "] 的方法 [" + methodName + parameterTypeClsName + "] 请检查方法名和方法调用参数是否指定正确");
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
            LOGGER.error("反射方法调用异常"+method,e.getTargetException());
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
     * 方法用途: 直接读取对象属性值 忽视private/protected修饰符，不经过getter函数<br>
     * 操作步骤: 注意：和invokeGetterMethod功能重复，后续确认抛弃掉其中一个方法 TODO<br>
     * @param obj
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> T obtainFieldValue(final Object obj, final String fieldName,Class<T> classz) {
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
     * 操作步骤: 注意：和invokeGetterMethod功能重复，后续确认抛弃掉其中一个方法 TODO
     *           和obtainFieldValue(final Object obj, final String fieldName,Class<T> classz)方法重叠,考虑是否去掉一个<br>
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object obtainFieldValue(final Object obj, final String fieldName) {
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
    public static Object newInstance(String className) {
		Class<?> clazz = ClassUtil.getClass(className);
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
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
    public static <T> T newInstance(Constructor<T> constructor, Object... args)  {
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
	 * 操作步骤: 默认不包含final的属性<br>
	 * @param bean 待转换的bean对象
	 * @return
	 */
	public static <T extends Object> Map<String, Object> bean2Map(T bean) {
		return bean2Map(bean,false);
	}
	
	/**
	 * 
	 * 方法用途: 实体转map<br>
	 * 操作步骤: TODO<br>
	 * @param bean 待转换的bean对象
	 * @param isContainFinal 是否包含final属性
	 * @return
	 */
	public static <T extends Object> Map<String, Object> bean2Map(T bean,boolean isContainFinal) {
//		Map<String, Object> returnMap = new ConcurrentHashMap<>();
		Map<String, Object> returnMap = new HashMap<>();//FIXED author:wanghb date:2016-05-20
		buildFieldInfo(bean.getClass(),bean,returnMap,isContainFinal);
		return returnMap;
	}
	
	/**
	 * 方法用途: 无论有多少超类，能递归判断和提取<br>
	 * 操作步骤: TODO<br>
	 * @param type
	 * @param bean 
	 * @param returnMap 
	 * @param isContainFinal 
	 */
	private static <T> void buildFieldInfo(Class<? extends T> type,T bean,Map<String,Object> returnMap,boolean isContainFinal) {
		Field[] fields = type.getDeclaredFields();
		for (Field field : fields) {
			if(!isContainFinal&&Modifier.isFinal(field.getModifiers())) {//不包含final字段，但是实际实体却包含，那么抛弃掉这个字段
				continue;
			}
			String fieldName = field.getName();
			Object fieldValue = invokeGetterMethod(bean, field.getName());
			returnMap.put(fieldName, fieldValue);
		}
		if(type.getSuperclass() != Object.class && type.getSuperclass() != null) {
			buildFieldInfo(type.getSuperclass(),bean,returnMap,isContainFinal);
		}
	}
	
//-----------------------获取class的基本信息-----------------------------
	/**
	 * 方法用途: 获取class的所有方法，包含所有父类的方法<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @return
	 */
	public static List<Method> getAllMethod(Class<?> entityClass) {
		List<Method> methodList = new ArrayList<>();
		getAllMethod(entityClass, methodList);
		return methodList;
	}
	
	/**
	 * 方法用途: 获取class的所有属性，包含父类的属性<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @param methodList
	 * @return
	 */
	private static void getAllMethod(Class<?> entityClass,List<Method> methodList) {
		Method[] methods = entityClass.getDeclaredMethods();
		for (Method method : methods) {
			Boolean isContains = CollectionUtil.contains(methodList, method,(m,w) -> method.getName().equals(m.getName()) );
			if(!isContains){
				methodList.add(method);
			}
		}
		if(entityClass.getSuperclass() != Object.class && entityClass.getSuperclass() != null) {
			getAllMethod(entityClass.getSuperclass(),methodList);
		}
	}
	
	/**
	 * 方法用途: 获取class的所有属性，包含父类的属性<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @param fieldList
	 * @return
	 */
	public static List<Field> getAllField(Class<?> entityClass) {
		List<Field> fieldList = new ArrayList<>();
		getAllField(entityClass, fieldList);
		return fieldList;
	}
	
	/**
	 * 方法用途: 获取class的所有属性，包含父类的属性<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @param fieldList
	 * @return
	 */
	private static void getAllField(Class<?> entityClass,List<Field> fieldList) {
		Field[] fields = entityClass.getDeclaredFields();
		Collections.addAll(fieldList, fields);
		if(entityClass.getSuperclass() != Object.class && entityClass.getSuperclass() != null) {
			getAllField(entityClass.getSuperclass(),fieldList);
		}
	}
}
