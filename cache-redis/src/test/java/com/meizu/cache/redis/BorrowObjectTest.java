package com.meizu.cache.redis;


public class BorrowObjectTest {
	
	
	 public static void main(String[] args) {   
	        for (int i = 0; i < 30000; i++) {   
	            new Thread(new BorrowObject()).start();   
	        }   
	    }   

}
