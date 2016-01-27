package com.meizu.simplify.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>反射泛型类型工具类</i></p>
 * <p>Desc: (例如：ObjectClass<String,Integer>，String为索引0，Integer为索引1)</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午4:38:59</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午4:38:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ReflectionGenericUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReflectionGenericUtil.class);
	
	
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
