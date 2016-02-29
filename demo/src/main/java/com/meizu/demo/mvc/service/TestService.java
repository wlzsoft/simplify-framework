package com.meizu.demo.mvc.service;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.demo.mvc.entity.User;
import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.annotation.CacheDataSearch;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestService {

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
        Test test = BaseDao.getIns(Test.class).findById(1);
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
        System.out.println("ִtest2测试2:"+test.getName());
        return test;
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
