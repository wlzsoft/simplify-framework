package vip.simplify.utils;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import vip.simplify.utils.model.UserModel;

/**
 * <p><b>Title:</b><i>泛型反射测试类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年2月22日 上午10:48:34</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年2月22日 上午10:48:34</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ReflectionGenericUtilTest {

	/**
	 * 
	 * 方法用途: 读取当前类的父级类上的泛型信息<br>
	 * 操作步骤: TODO<br>
	 * @throws Exception
	 */
	@Test
	public void getSuperClassGenricTypeForFirstTest() throws Exception {
		Object entityClazz = ReflectionGenericUtil.getSuperClassGenricTypeForFirst(InnerImpl.class);
		System.out.println(entityClazz);
	}
	
	/**
	 * 
	 * 方法用途: 读取方法的泛型参数类型信息<br>
	 * 操作步骤: TODO<br>
	 * @throws Exception
	 */
	@Test
	public void getGenricParamByMethodForFirstTest() throws Exception {
		Object entityClazz = ReflectionGenericUtil.getGenricTypeParam(UserModel.class.getMethods()[0], 0, 0);
		System.out.println(entityClazz);
	}
	
	Map<String, Integer> map;
	Inner<Float, Double> inner;
	List<Set<String>[][]> listBySetByString;
	List<String> listByString;
	Set<String> setByString;
	Outer<String>.Inner innerByOuterByString;
	
	/**
	 * 
	 * 方法用途: 读取当前类成员变量的泛型信息<br>
	 * 操作步骤: TODO<br>
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		 Field[] fs = this.getClass().getDeclaredFields();
		 for(Field field : fs) { 
		     Class<?> fieldClazz = field.getType(); 
		     if(fieldClazz.isPrimitive()) {//是否为基本类型
		    	 continue;
		     }
		     if(fieldClazz.getName().startsWith("java.lang")) {
		    	 continue;
		     }
		     Type type = field.getGenericType(); // 获取属性的泛型类型
		     if(type == null) {
		    	 continue;
		     }
             if(type instanceof ParameterizedType) {//如果是泛型参数的类型 
                   ParameterizedType pt = (ParameterizedType) type;
                   Type[] types = pt.getActualTypeArguments();
                   String ret = field.getName()+","+fieldClazz.getName()+"\n\t" + pt + "\n\t\t泛型个数：" + types.length + "==>";
                   for (Type genericClazz : types) {//得到泛型里的class类型对象
                	   if(genericClazz instanceof GenericArrayType) {
                		   GenericArrayType setStringArray = (GenericArrayType)genericClazz;
                		   System.out.println("c List<Set<String>[][]> List第二层Set的泛型推导信息：" + setStringArray);
                	   }
                	   ret += genericClazz + ", ";
                   }
                   if(fieldClazz.isAssignableFrom(List.class)) { 
                	   System.out.println(ret);
                   } else {
                	   System.out.println(ret);
                   }
                   System.out.println();
		     }
		}
	}
	
	class InnerImpl extends Inner<Integer,String> {
		
	}

	class Inner<T1, T2> {
		
	}

	static class Outer<T> {
		class Inner {
		}
	}
}
