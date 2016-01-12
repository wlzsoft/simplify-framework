package com.meizu.cache.redis;

import java.io.Serializable;

import com.meizu.cache.redis.ListCacheClient;
import com.meizu.stresstester.StressTestUtils;
import com.meizu.stresstester.core.StressTask;

public class ListMainTest {
	public static ListCacheClient client = new ListCacheClient("redis_ref_hosts");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StressTestUtils.testAndPrint(100, 1000, new StressTask(){

			@Override
			public Object doTask() throws Exception {
				testListInsert();
				return null;
			}
			
		});

	}
	
	public static void testListGet(){
		String key = "producer_list1";
		//long begin = System.currentTimeMil	lis();
	//	List list = client.lrange(key,0,100);
//		client.lpop(key);
		System.out.println(client.lpop(key));
		//System.out.println(list.size());
		//System.out.println("time:"+(System.currentTimeMillis()-begin));
	}
	
	public static void testListInsert(){
		
		String key = "producer_list";
		User usr = new User("101001","testname");
		usr.setAddr("sfsdfsfff");
		usr.setPhone("131321324234324");
		client.lpush(key, "{'name':'lcy',id:1}");
	}
	
	private static class User implements Serializable{
		private String id;
		private String name;
		private String addr;
		private String phone;
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public User(String id,String name){
			this.id = id;
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

}
