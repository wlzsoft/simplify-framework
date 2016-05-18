package com.meizu.simplify.cache;

import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.annotation.CacheDataDel;
import com.meizu.simplify.cache.annotation.CacheDataSearch;
import com.meizu.simplify.cache.entity.User;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class UserService {
		
		@CacheDataSearch(key="ttt3")
		public User getUser(Integer id) {
			User user = new User();
			return user;
		}
		@CacheDataAdd(key="ttt2")
		public void addUser(User user) {

		}
		@CacheDataDel(key="ttt")
		public void delUser(Integer id) {

		}
		
		@CacheDataAdd(key="aaa")
	    public Object doSomeThing(User bb) {
	        System.out.println("ִtest2测试");
	        return true;
	    }
		@CacheDataAdd(key="bbb")
	    public Object doSomeThing2(String aa) {
	        System.out.println("ִtest2测试2");
	        return true;
	    }

	}