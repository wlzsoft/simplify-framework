package com.meizu.demo.mvc.service;

import java.io.IOException;
import java.util.Map;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.annotation.CacheDataSearch;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.dao.annotations.Transation;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.ITemplate;

@Bean
public class TestService {
	@Config("system.debug")
	private boolean debug;
	@Config
	private boolean cache;
	@CacheDataAdd(key="aaa")
    public Object doSomeThing() {
        System.out.println("2test2d测试");
        return true;
    }
	/**
	 * 
	 * 方法用途: 不能同时有两个cache相关注解，前者会被后者覆盖<br>
	 * 操作步骤: TODO<br>
	 * @param aa
	 * @return
	 */
//	@CacheDataAdd(key="bbb")
	@CacheDataSearch(key="bbb")
    public Test doSomeThing2() {
		System.out.println("debug:"+debug+"|cache:"+cache);
		System.out.println("configtest:"+BeanFactory.getBean(PropertiesConfig.class).getProp().getInteger("configtest"));
        Test test = BaseDao.getIns(Test.class).findById(1);
        if(test == null) {
        	return null;
        }
        System.out.println("ִtest2测试2:"+test.getName());
        return test;
    }
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param test 写入redis，是写入的这个参数
	 * @return
	 */
	@CacheDataAdd(key="bbb")
	public Test addTest(Test test) {
        test = BaseDao.getIns(Test.class).findById(1);
        if(test == null) {
        	System.out.println("ִnull测试2:");
        } else {
        	System.out.println("ִtest2测试2:"+test.getName());
        }
        return test;
    }
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param test 写入redis，是写入的这个参数
	 * @return
	 */
	@Transation
	@CacheDataAdd(key="bbb22")
	public void addTestObj(Test test) {
        test = BaseDao.getIns(Test.class).findById(1);
        System.out.println("ִ测试添加功能:"+test.getName());
    }
	@Resource
	private ITemplate template;
	public void testSqlTemplate() throws IOException {
		Map<String, Object> parameters = null;
		String sql = template.render(parameters, "test", "/com/meizu/demo/mvc/dao/","sql");
//		Test test = BaseDao.getInsPojo().find(Test.class, sql).get(0);
        System.out.println("ִ测试sql模版:"+sql);
    }


    /*public static void main(String[] args) {
    	long start = System.currentTimeMillis();
        TestService h = new TestService();
        User bb = new User();
        bb.setName("yyyyy");
        h.doSomeThing(bb);
        System.out.println(System.currentTimeMillis()-start);
    }
*/
}
