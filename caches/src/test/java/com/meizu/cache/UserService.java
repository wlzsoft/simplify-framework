package com.meizu.cache;

import com.meizu.cache.annotation.CacheDataAdd;
import com.meizu.cache.annotation.CacheDataDel;
import com.meizu.cache.annotation.CacheDataSearch;
import com.meizu.cache.entity.User;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class UserService {
		
		@CacheDataSearch
		public User getUser(Integer id) {
			User user = new User();
			return user;
		}
		@CacheDataAdd(key="#id")
		public void addUser(User user) {

		}
		@CacheDataDel
		public void delUser(Integer id) {

		}

	}