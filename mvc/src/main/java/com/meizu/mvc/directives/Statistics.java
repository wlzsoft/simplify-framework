package com.meizu.mvc.directives;

import com.meizu.mvc.FiFoMap;

/**
 * MVC性能统计
 * 
 */
public class Statistics {
	private static long readcount = 0;
	private static long readMaxTime = 0;
	private static String readMaxTimeUrl;
	private static FiFoMap<String, Integer> readMap = new FiFoMap<String, Integer>(128);
	
	private Statistics() {
		
	}
	
	public static long getReadcount() {
		return readcount;
	}
	
	public static void setReadcount(long readcount) {
		Statistics.readcount = readcount;
	}
	
	public static void incReadcount() {
		Statistics.readcount++;
	}
	
	public static long getReadMaxTime() {
		return readMaxTime;
	}
	
	public static void setReadMaxTime(long readMaxTime) {
		Statistics.readMaxTime = readMaxTime;
	}
	
	public static String getReadMaxTimeUrl() {
		return readMaxTimeUrl;
	}
	
	public static void setReadMaxTimeUrl(String readMaxTimeUrl) {
		Statistics.readMaxTimeUrl = readMaxTimeUrl;
	}
	
	public static void setReadMaxTime(long readMaxTime, String readMaxTimeUrl) {
		if (Statistics.readMaxTime < readMaxTime) {
			Statistics.readMaxTime = readMaxTime;
			Statistics.readMaxTimeUrl = readMaxTimeUrl;
		}
	}
	
	public static FiFoMap<String, Integer> getReadMap() {
		return readMap;
	}
	
}
