package com.meizu.aop;

import model.Business;

/**
 * 自定义的类加载
 * @author Administrator
 *
 */
public class ClassLoadStartup  extends ClassLoader{
	
	public static void main(String[] args) {
    	long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        try {
        	ClassLoadStartup myClassloader = new ClassLoadStartup();
            Class b = myClassloader.loadClass("model.Business");
            ((Business) b.newInstance()).doSomeThing();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println((end-start)+ "ms");

    }

}
