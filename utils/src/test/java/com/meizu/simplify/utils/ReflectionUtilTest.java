package com.meizu.simplify.utils;

import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.demo.DemoService;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 下午7:07:02</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 下午7:07:02</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
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
	public void getSuperClassGenricTypeForObject() {
		class SuperClassGenricType {
			public Class<? extends SuperClassGenricType> entityClass;
		    public void getBaseRedisDao(){
		        this.entityClass = ReflectionUtil.getSuperClassGenricType(getClass(),0);
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
		
	}
	@Test
	public void getSuperClassGenricTypeForFirst() {
		class ParentCen<T,A> {
			
		}
		class SuperClassGenricType extends ParentCen<Integer,Boolean> {
			public Class<? extends SuperClassGenricType> entityClass;
		    public void getBaseRedisDao(){
		        this.entityClass = ReflectionUtil.getSuperClassGenricTypeForFirst(getClass());
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
		        this.entityClass = ReflectionUtil.getSuperClassGenricType(getClass());
		    }
		    public  int test() {
		    	getBaseRedisDao();
		    	return entityClass.length;
			}
		}
		SuperClassGenricType obj = new SuperClassGenricType();
		Assert.assertEquals(2, obj.test());
		
	}
	

}
