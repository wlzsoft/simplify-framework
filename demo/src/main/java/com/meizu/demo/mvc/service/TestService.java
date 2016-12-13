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
import com.meizu.simplify.dao.orm.Dao;
import com.meizu.simplify.dao.template.SqlTemplateFactory;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.log.Logger;

@Bean
public class TestService {
	@Config("system.debug")
	private Boolean debug;
	@Config("aa")
	private String aa;
	@Config
	private boolean unicodeTranscoding = false;
	
	@Resource//(name="com.meizu.demo.mvc.service.TestServiceLog")
	private Logger logger;
	
	@Resource
	private Dao<Test,Integer> dao;
	
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
//	@CacheDataAdd(key="bbb")//不可以同时使用两个缓存相关的注解，否则会被覆盖掉,以最后一个为准
	@CacheDataSearch(key="bbb")
    public Test doSomeThing2(Test test) {
		System.out.println("debug:"+debug+"|unicodeTranscoding:"+unicodeTranscoding);
		System.out.println("cache.key.timeout:"+BeanFactory.getBean(PropertiesConfig.class).getProp().getInteger("cache.key.timeout"));
        test = BaseDao.getIns(Test.class).findById(1);
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
//	@Transation(ISO=ISOEnum.TRANSACTION_REPEATABLE_READ)
	@Transation
//	@CacheDataSearch(key="bbbt")
	@CacheDataAdd(key="bbb")
	public Test addTest(Test test) {
		if(logger != null) {
			logger.info("测试日志输出2");
		}
		test = dao.findById(1);
		if(test != null) {
	       	System.out.println("ִdao属性方式注入测试2:"+test.getName());
        }
        test = BaseDao.getIns(Test.class).findById(1);
        if(test == null) {
        	System.out.println("ִnull测试2:");
        } else {
        	System.out.println("ִtest2测试2:"+test.getName());
//        	throw new RuntimeException("事务异常测试");
        }
        test = new Test();
        test.setName("hahaha");
        BaseDao.getIns(Test.class).save(test);
        return test;
    }
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param test 写入redis，是写入的这个参数
	 * @return
	 */
	@CacheDataAdd(key="bbb22")
	public void addTestObj(Test test) {
        test = BaseDao.getIns(Test.class).findById(1);
        System.out.println("ִ测试添加功能:"+test.getName());
    }
	public void testSqlTemplate() throws IOException {
		Map<String, Object> parameters = null;
		Test test = BaseDao.getInsPojo().find(Test.class, SqlTemplateFactory.getSql("test", parameters)).get(0);
        System.out.println("ִ测试sql模版:"+test.getName());
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
