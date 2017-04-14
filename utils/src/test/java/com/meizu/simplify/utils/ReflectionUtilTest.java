package com.meizu.simplify.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.meizu.simplify.utils.entity.User;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.demo.DemoService;
import com.meizu.simplify.utils.entity.Test2;
import com.meizu.simplify.utils.entity.TestImpl;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 下午7:07:02</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 下午7:07:02</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ReflectionUtilTest  {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void invokeMethod() {
		Object obj = new DemoService();
		Assert.assertEquals("lcy",ReflectionUtil.invokeMethod(obj, "demoMessage"));
	}
	
	@Test
	public void invokeSetMethod() {
		Object obj = new Test2();
		ReflectionUtil.invokeSetterMethod(obj, "name", 1,true);
		Assert.assertEquals("1",((Test2)obj).getName());
	}
	
	@Test
	public void getSuperClassGenricTypeForObject() {
		class SuperClassGenricType {
			public Class<? extends SuperClassGenricType> entityClass;
		    public void getBaseRedisDao(){
		        this.entityClass = ReflectionGenericUtil.getSuperClassGenricType(getClass(),0);
		    }
		    public  String test() {
		    	getBaseRedisDao();
		    	return entityClass.toString();
			}
		}
		expectedException.expect(UncheckedException.class);
		expectedException.expectMessage("SuperClassGenricType的父类是Object");
		SuperClassGenricType obj = new SuperClassGenricType();
		String test = obj.test();
		System.out.println(test);
		
	}
	@Test
	public void getSuperClassGenricTypeForFirst() {
		class ParentCen<T,A> {
			
		}
		class SuperClassGenricType extends ParentCen<Integer,Boolean> {
			public Class<? extends SuperClassGenricType> entityClass;
		    public void getBaseRedisDao(){
		        this.entityClass = ReflectionGenericUtil.getSuperClassGenricTypeForFirst(getClass());
		    }
		    public  String test() {
		    	getBaseRedisDao();
		    	return entityClass.toString();
			}
		}
		SuperClassGenricType obj = new SuperClassGenricType();
		String test = obj.test();
		Assert.assertEquals("class java.lang.Integer", test);
		
	}
	
	@Test
	public void getSuperClassGenricType() {
		class ParentCen<T,A> {
			
		}
		class SuperClassGenricType extends ParentCen<Integer,Boolean> {
			public Type[] entityClass;
		    public void getBaseRedisDao(){
		        this.entityClass = ReflectionGenericUtil.getSuperClassGenricTypeArray(getClass());
		    }
		    public  int test() {
		    	getBaseRedisDao();
		    	return entityClass.length;
			}
		}
		SuperClassGenricType obj = new SuperClassGenricType();
		Assert.assertEquals(2, obj.test());
		
	}
	
	@Test
	public void beanToMapTest() {
		
		Test2 test = new Test2();
		test.setId(1);
		test.setName("lcy");
		Map<String, Object> paramMap = ReflectionUtil.bean2Map(test);
		Set<Entry<String, Object>> set = paramMap.entrySet();
		for (Entry<String, Object> entry : set) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
	}
	@Test
	public void beanToMapByFinalTest() {
		
		Test2 test = new Test2();
		test.setId(1);
		test.setName("lcy");
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("不能找到对象 [" + test + "] 的方法 [getSeri()] 请检查方法名和方法调用参数是否指定正确");
		Map<String, Object> paramMap = ReflectionUtil.bean2Map(test,true);
		Set<Entry<String, Object>> set = paramMap.entrySet();
		for (Entry<String, Object> entry : set) {
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
	}
	@Test
	public void getAllField() {
		
		List<Field> list = ReflectionUtil.getAllField(TestImpl.class);
		for (Field field : list) {
			System.out.println(field.getName());
		}
		
	}
	@Test
	public void getAllMethod() {
		
		List<Method> list = ReflectionUtil.getAllMethod(TestImpl.class);
		for (Method field : list) {
			System.out.println(field.getName());
		}
		
	}

	@Test
	public void getTypeToken() {
		Type userListType = ReflectionGenericUtil.getSuperClassGenricType(new ReflectionGenericUtil.TypeToken<List<User>>() {
		});
		Assert.assertEquals("java.util.List<com.meizu.simplify.utils.entity.User>",userListType.getTypeName());

	}
	
	@Test
	public void invoke(){
		User user = new User();
		user.setName("lcy");
		String name = (String) ReflectionUtil.invoke(user, "getName");
		Assert.assertEquals(name, user.getName());
		
	}
	
	@Test
	public void invokeByParam(){
		User user = new User();
		ReflectionUtil.invoke(user, "setName","aa");
		Assert.assertEquals(user.getName(), "aa");
	}
	
	@Test
	public void isExtendOrSelfClass(){
		Assert.assertTrue(ReflectionUtil.isExtendOrSelfClass(TestImpl.class, Test2.class));
		Assert.assertTrue(ReflectionUtil.isExtendOrSelfClass(Object.class, Object.class));
		Assert.assertFalse(ReflectionUtil.isExtendOrSelfClass(Object.class, Test2.class));
		Assert.assertTrue(ReflectionUtil.isExtendOrSelfClass(Test2.class, Object.class));
		Assert.assertTrue(ReflectionUtil.isExtendOrSelfClass(TestImpl.class, TestImpl.class));
		Assert.assertTrue(ReflectionUtil.isExtendOrSelfClass(Test2.class, Test2.class));
	}

}
