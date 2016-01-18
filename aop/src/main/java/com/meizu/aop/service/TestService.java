package com.meizu.aop.service;

public class TestService {

    public boolean doSomeThing() {
        System.out.println("ִtest测试");
        return true;
    }
    
    public boolean doSomeThing2() {
        System.out.println("ִtest测试2");
        return true;
    }


    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
        TestService h = new TestService();
        h.doSomeThing();
        System.out.println(System.currentTimeMillis()-start);
    }

}
