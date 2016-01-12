package com.meizu.cache.redis;

import com.meizu.cache.redis.client.impl.JsonCacheClient;

public class BorrowObject implements Runnable {
	 private JsonCacheClient client = new JsonCacheClient("redis_ref_hosts")  ;
	  
	  
	    @Override  
	    public void run() {   
	        try {   
	           client.set("time", "LONG_KEY_TEST:AA059E03E0AB7D806E6C351F87404B06C1190", -1); 
	            System.out.println(System.currentTimeMillis());   
	        } catch (Exception e) {   
	            e.printStackTrace();   
	        } 
	    }   

}
