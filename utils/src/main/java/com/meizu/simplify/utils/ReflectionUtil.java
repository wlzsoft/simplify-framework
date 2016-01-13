package com.meizu.simplify.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
public class ReflectionUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
    private ReflectionUtil() {
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
        	throw new IllegalArgumentException("不能找到对象 [" + obj + "] 的方法 [" + methodName + "] 请检查方法名和方法调用参数是否指定正确"); 
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
     * 操作步骤: TODO<br>
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
     * 方法用途: 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br>
     * 操作步骤: TODO <br>
     * @param 需要解析的class类型
     * @return 返回第一个位置的泛型的class
     */
    public static <T> Class<T> getSuperClassGenricTypeForFirst(final Class<T> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }
    
   
    /**
     * 方法用途: 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br>
     * 操作步骤: TODO <br>
     * @param 需要解析的class类型
     * @param 指定实现父类的泛型的参数的位置
     * @return 返回具体指定index值位置的泛型的class
     */
    @SuppressWarnings("unchecked")
	public  static <T> Class<T> getSuperClassGenricType(final Class<T> clazz, final int index) {

        Type[] params = getSuperClassGenricType(clazz);

        if (index >= params.length || index < 0) {
            LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            throw new UncheckedException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数")); 
        }
        if (!(params[index] instanceof Class)) {
            LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            throw new UncheckedException(clazz.getSimpleName() + " 超类的泛型参数没有指定具体类类型"); 
        }

        return (Class<T>) params[index];
    }


	/**
	 * 
	 * 方法用途: 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 需要解析的class类型
	 * @return 返回泛型的class 列表
	 */
	public static <T> Type[] getSuperClassGenricType(final Class<T> clazz) {
		Type genType = clazz.getGenericSuperclass();// 得到泛型父类
        if (genType.getTypeName().equals("java.lang.Object")) {
        	throw new UncheckedException(clazz.getSimpleName() + "的父类是Object"); 
        }
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
//            如果没有实现ParameterizedType接口，即不支持泛型，抛异常
            throw new UncheckedException(clazz.getSimpleName() + "没有指定实现父类的泛型参数"); 
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如public class ExpireRedisDao<K,V> extends BaseRedisDao<Integer,Serializable>就返回Integer和Serializable类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return params;
	}

}
