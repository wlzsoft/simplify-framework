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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ReflectionGenericUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReflectionGenericUtil.class);
	
//	----------------------------获取class的父类的泛型参数的类型-------------------------------
	
	/**
     * 方法用途: 通过反射, 获得Class定义中声明的父类的第一个泛型参数的类型,如无法找到, 返回Object.class<br>
     * 操作步骤: TODO 后续需要做单元测试支撑<br>
     * @param clazz 需要获取实现父类的泛型参数的源类
     * @return 返回第一个泛型参数的声明, 如果父类没有泛型参数，那么返回Object.class
     */
    public static <T> Class<T> getSuperClassGenricTypeForFirst(final Class<T> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }
    
    /**
     * 方法用途: 通过反射, 获得Class定义中声明的父类声明的泛型参数的类型,如无法找到, 返回Object.class<br>
     * 操作步骤: 如public UserDao extends Dao<User,Long>， 返回的是Class<User>或是Class<Long>,具体根据index参数来决定 <br>
     * @param clazz 需要获取实现父类的泛型参数的需要解析的class源类型
     * @param index 这个参数值从0开始，用于选择需要返回的实现类或接口的泛型参数的索引位置
     * @return 返回指定索引(index值位置)的泛型参数的声明的class, 如果父类没有泛型参数，那么返回Object.class
     */
    @SuppressWarnings("unchecked")
	public  static <T> Class<T> getSuperClassGenricType(final Class<?> clazz, final int index) {

        Type[] params = getSuperClassGenricTypeArray(clazz);

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
	public static <T> Type[] getSuperClassGenricTypeArray(final Class<T> clazz) {
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
