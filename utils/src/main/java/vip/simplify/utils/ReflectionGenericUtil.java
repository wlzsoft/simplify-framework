package vip.simplify.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.exception.UncheckedException;

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
    public static <T> Class<T> getSuperClassGenricTypeForFirst(final Class<?> clazz) {
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
     * 方法用途: 读取方法的泛型参数类型信息<br>
     * 操作步骤: TODO 待增加单元测试 <br>
     * @param method
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T> Class<T> getGenricTypeParam(Method method, final int paramIndex, final int paramGenericIndex) {
    	String name = method.getName();
    	Type[] params = method.getGenericParameterTypes();
		if (paramIndex >= params.length || paramIndex < 0) {
            LOGGER.warn("Index: " + paramIndex + ", Size of " + name + "'s Method Param Type: "  + params.length);
            throw new UncheckedException("你输入的索引" + (paramIndex < 0 ? "不能小于0" : "超出了"+name+"方法参数的总数")); 
        }
        if (!(params[paramIndex] instanceof ParameterizedType)) {
            LOGGER.warn(name + "'s param not ParameterizedType");
            throw new UncheckedException(name + " 方法的泛型参数没有指定ParameterizedType类型"); 
        }
        ParameterizedType parameterizedType = (ParameterizedType) params[paramIndex];
        Type[] paramGeneric = parameterizedType.getActualTypeArguments();
        if (paramGenericIndex >= paramGeneric.length || paramGenericIndex < 0) {
            LOGGER.warn("Index: " + paramGenericIndex + ", Size of " + name + "'s Parameterized Type: "
                    + paramGeneric.length);
            throw new UncheckedException("你输入的索引" + (paramGenericIndex < 0 ? "不能小于0" : "超出了参数的总数")); 
        }
        if (!(paramGeneric[paramGenericIndex] instanceof Class)) {
            LOGGER.warn(name + " not set the actual class on method param generic parameter");
            throw new UncheckedException(name + " 方法的泛型参数没有指定具体泛型的类型"); 
        }
        return (Class<T>) paramGeneric[paramGenericIndex];
    }

	/**
	 * 
	 * 方法用途: 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 需要解析的class类型
	 * @return 返回泛型的class 列表
	 */
	public static <T> Type[] getSuperClassGenricTypeArray(final Class<T> clazz) {
		Type genType = clazz.getGenericSuperclass();// 得到泛型父类的对象[指定是Class<T>的父类Type的对象genType]
        if (genType.getTypeName().equals("java.lang.Object")) {
        	throw new UncheckedException(clazz.getSimpleName() + "的父类是Object"); 
        }
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
//            如果没有实现ParameterizedType接口，即不支持泛型，抛异常
            throw new UncheckedException(clazz.getSimpleName() + "没有指定实现父类的泛型参数"); 
        }
        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如public class ExpireRedisDao<K,V> extends BaseRedisDao<Integer,Serializable>就返回Integer和Serializable类型
        ParameterizedType parameterized = (ParameterizedType)genType;
        Type[] params = parameterized.getActualTypeArguments();
		return params;
	}
    /**
     *
     * 方法用途: 通过反射, 获得Class定义中声明的父类的泛型参数的类型<br>
     * 操作步骤: 注意：该方法不能在运行时多次使用，只用于启动或初始化时<br>
     * @param typeToken 需要解析的TypeToken类型
     * @return 返回泛型的Type 列表
     */
    public static <T> Type getSuperClassGenricType(TypeToken<T> typeToken) {
        return typeToken.getType();
    }
    /**
     * <p><b>Title:</b><i>反射泛型类型标记类</i></p>
     * <p>Desc: 用于承载泛型Type的对象的信息</p>
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
    public static class TypeToken<T> {
        Type type;

        /**
         * 构造函数必须是受保护(protected)，new的时候必须是空的内部实现类，才能获取到泛型值
         */
        protected TypeToken() {
            this.type = getSuperClassGenricTypeArray(this.getClass())[0];
        }
        private Type getType() {
            return type;
        }
    }
}


