package vip.simplify.cache;

import vip.simplify.cache.annotation.CacheDataAdd;
import vip.simplify.cache.annotation.CacheDataDel;
import vip.simplify.cache.annotation.CacheDataSearch;
import vip.simplify.cache.entity.User;
import vip.simplify.ioc.annotation.Bean;

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