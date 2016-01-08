package com.meizu.simplify.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.UncheckedException;


/**
 * <p><b>Title:</b><i>反射工具类.</i></p>
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
public class ReflectionUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);
    private ReflectionUtils() {
    }
    
    
    /**
     * 调用Getter方法
     * BeanMapUtil.bean2Map(t) 相似 TODO
     * @param obj
     *            对象
     * @param propertyName
     *            属性名
     * @return
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName,propertyName, null, null);
//        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }
     
     
    /**
     * 调用Setter方法,指定参数的类型
     * 
     * @param obj
     * @param propertyName  字段名
     * @param value
     * @param propertyType 用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        value = handleValueType(obj,propertyName,value);
        propertyType = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName,propertyName, new Class<?>[] { propertyType }, new Object[] { value });
    }
    
    /**
     * 直接调用对象方法，忽视private/protected修饰符
     * 
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param params
     * @return
     */
    public static Object invokeMethod(final Object obj, final String methodName,final String fieldName, final Class<?>[] parameterTypes, final Object[] args) {//TODO
        Method method = obtainAccessibleMethod(obj, methodName,fieldName, parameterTypes);
        if (method == null) {
        	throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]"); 
        }
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new UncheckedException("参数类型不匹配！");//后续再补充提示信息，指明源数据类型和目录属性类型 TODO
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * 方法用途: 调用Setter方法.使用value的Class来查找Setter方法,不指定参数的类型.<br>
     * 操作步骤: TODO<br>
     * @param obj
     * @param propertyName
     * @param value
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }


    public static String getAnnotatedFieldName(Class clazz, Class<? extends Annotation> annotation) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                return field.getName();
            }
        }
        return null;
    }

    
    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = obtainAccessibleField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
 
        makeAccessible(field);
 
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LOGGER.error("不可能抛出的异常{}", e.getMessage());
        }
        
        return result;
    }
       
    
    /**
     * 直接设置对象属性值 忽视private/protected修饰符，不经过setter函数
     * 
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
    	
        Field field = obtainAccessibleField(obj, fieldName);
        if (field == null) { 
        	throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        makeAccessible(field); //TODO
        try {
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error("不可能抛出的异常:{}", e.getMessage());
        }

    }


    /**
     * 
     * 需要重构这个方法：TODO ，fieldName 和 parameterTypes有些冗余
     * 循环向上转型，获取对象的DeclaredMethod,并强制设置为可访问 如向上转型到Object仍无法找到，返回null
     * 
     * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj,Object...
     * args)
     * 
     * @param obj
     * @param methodName
     * @param fieldName
     * @param parameterTypes
     * @return
     */
    public static Method obtainAccessibleMethod(final Object obj,  final String methodName,final String fieldName, final Class<?>... parameterTypes) {
    	Assert.notNull(obj, "object不能为空");
        Class<?> superClass = obj.getClass();
        Class<Object> objClass = Object.class;
        for (; superClass != objClass; superClass = superClass.getSuperclass()) {
            Method method = null;
            try {
            	if(fieldName != null) {
            		Field field = superClass.getDeclaredField(fieldName);//定制内容，只有针对pojo的方法才有效
            		String type = field.getGenericType().toString();
            		if(parameterTypes != null&&parameterTypes.length>0) {
            			
            			if("PK".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						Serializable.class);//TODO bug  Serializable.class 需要一个泛型
            			}
            			if("class java.lang.Boolean".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						Boolean.class);
            			}
            			if("boolean".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						boolean.class);
            			}
            			if("class java.lang.Integer".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						Integer.class);
            			}
            			if("int".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						int.class);
            			}
            			if("class java.lang.Double".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						Double.class);
            			}
            			if("double".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						double.class);
            			}
            			if("class java.lang.Float".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						Float.class);
            			}
            			if("float".equals(type)) {
            				method = superClass.getDeclaredMethod(methodName,
            						float.class);
            			}
            			if(method != null) {
            				method.setAccessible(true);
            				return method;
            			}
            		} 
            	} 
            	method = superClass.getDeclaredMethod(methodName,
            				parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {
            	continue;// Method不在当前类定义,继续向上转型
            } catch (SecurityException e) {
            	continue;
            } catch (NoSuchFieldException e) {
            	continue;
            }
        }
        return null;
    }
    
 
   
    
    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }
    
//    @SuppressWarnings("unchecked")
//    public static <T> Class<T> getSuperClassGenricType(final Class<T> clazz) {
//        return getSuperClassGenricType(clazz, 0);
//    }
   
    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p/>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public  static <T> Class<T> getSuperClassGenricType(final Class<T> clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();// 得到泛型父类
        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
//            return Object.class;
            throw new UncheckedException(clazz.getSimpleName() + "'s superclass not ParameterizedType"); // TODO
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            throw new UncheckedException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数")); // TODO
//            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
//            return Object.class;
            throw new UncheckedException(clazz.getSimpleName() + " not set the actual class on superclass generic parameter"); // TODO
        }

        return (Class<T>) params[index];
    }

    
//    ----------------------------------------
    
   
    /**
     * 缓存方法
     */
    private static final Map<Class<?>, Method[]>    METHODS_CACHEMAP    = new HashMap<Class<?>, Method[]>();
     
    /**
     * 反射 取值、设值,合并两个对象(Field same only )
     * 
     * @param from
     * @param to
     */
    public static <T> void copyProperties(T fromobj, T toobj,
            String... fieldspec) {
        for (String filename : fieldspec) {
            Object val = ReflectionUtils.invokeGetterMethod(fromobj, filename);
            ReflectionUtils.invokeSetterMethod(toobj, filename, val);
        }
         
    }
     
   
     
    private static Object handleValueType(Object obj, String propertyName,
            Object value){
    	String methodTypeString = "get";
        String getterMethodName = methodTypeString + StringUtils.capitalize(propertyName);
        Method method = obtainAccessibleMethod(obj, getterMethodName,propertyName);
        if(method == null) {
        	methodTypeString = "is";
        	getterMethodName = methodTypeString + StringUtils.capitalize(propertyName);
        	method = obtainAccessibleMethod(obj, getterMethodName,propertyName);
        }
        if(method == null) {
        	throw new UncheckedException("无法找到属性"+propertyName+"对应的"+methodTypeString+"方法，请确认是否有存在规范的"+methodTypeString+"方法，比如"+methodTypeString+"方法中的属性名第一个字母要大写。");
        }
        Class<?> returnType = method.getReturnType();
        Class<?> argsType = value.getClass();
        if(argsType == returnType){
            return value;
        }
         
        if (returnType == Boolean.class||returnType.toString().equals("boolean")) {
            String temp = value.toString();
            value = (StringUtils.isNotBlank(temp) && Long.valueOf(temp) > 0) ? true : false;
        } else if (returnType == Long.class) {
            value = Long.valueOf(value.toString());
        }else if(returnType == Date.class){
            try {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	if(value.toString().indexOf(" ") != -1){
            		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	}else{
            		sdf = new SimpleDateFormat("yyyy-MM-dd");
            	}
            	value =sdf.parse(value.toString());
            } catch (ParseException e) {
                LOGGER.error("类型转型Timpestap-->Date时，发生错误! " + e.getMessage() + "("+value.toString()+")");
            }
        } else if (returnType == Short.class) {
            value = Short.valueOf(value.toString());
        } else if (returnType == BigDecimal.class) {
            value = BigDecimal.valueOf(Long.valueOf(value.toString()));
        } else if (returnType == BigInteger.class) {
            value = BigInteger.valueOf(Long.valueOf(value.toString()));
        } else if(returnType == String.class){
            value = String.valueOf(value);
        }else if(returnType == Integer.class){
            value = Integer.valueOf(value.toString()); 
        }
        return value;
    }
     
   
     
   
     
    /**
     * 不能确定方法是否包含参数时，通过方法名匹配获得方法
     * 
     * @param obj
     * @param methodName
     * @return
     */
    public static Method obtainMethod(final Object obj, final String methodName) {
        Class<?> clazz = obj.getClass();
        Method[] methods = METHODS_CACHEMAP.get(clazz);
        if (methods == null) { // 尚未缓存
            methods = clazz.getDeclaredMethods();
            METHODS_CACHEMAP.put(clazz, methods);
        }
        for (Method method : methods) {
            if (method.getName().equals(methodName))
                return method;
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
     * 直接读取对象属性值 忽视private/protected修饰符，不经过getter函数
     * 
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
//         TODO 待整理的方法
//        MetaInfo metaInfo;
//  		try {
//  			metaInfo = objectToMap(obj).get(fieldName);
//  			return metaInfo.getValue();
//  		} catch (IllegalAccessException | IllegalArgumentException
//  				| InvocationTargetException | IntrospectionException e) {
//  			// TODO Auto-generated catch block
//  			e.printStackTrace();
//  		}
//  		return null;
         
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
     * 循环向上转型，获取对象的DeclaredField,并强制设为可访问 如向上转型Object仍无法找到，返回null
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field obtainAccessibleField(final Object obj, final String fieldName) {
    	Assert.notNull(obj, "object不能为空");
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
     * 强制转换fileld可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
 
   
 
	
	//----------------------------------------------
	public static Object Map2Bean(Class<?> type, Map<String, Object> map)
			throws IntrospectionException, IllegalAccessException,
			InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		Object obj = type.newInstance();

		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();

		for (int i = 0; i < propertyDescriptors.length; ++i) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!(map.containsKey(propertyName.toUpperCase())))
				continue;
			try {
				Object value = map.get(propertyName.toUpperCase());
				setFieldValue(obj, propertyName, value);
			} catch (Exception e) {
			}
		}
		return obj;
	}

	/**
	 *  ReflectionUtils.invokeGetterMethod(t, idName);  类似用法 TODO
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Map<String, Object> bean2Map(Object bean)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Class<?> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; ++i) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!(propertyName.equals("class"))) {
				Method readMethod = descriptor.getReadMethod();
				if(readMethod == null) {
					//returnMap.put(propertyName, "");
					continue;
				}
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null)
					returnMap.put(propertyName, result);
				else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}
}
