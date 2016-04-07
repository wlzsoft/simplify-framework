package com.meizu.simplify.cache;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.entity.User;
import com.meizu.simplify.cache.resolver.CacheAnnotationResolver;
import com.meizu.simplify.dto.AnnotationInfo;

import javassist.CannotCompileException;

public class CacheSimpleTest {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, CannotCompileException {
//		CtClass cc = new AopClassFileTransformer().buildClazz("com/meizu/aop/service/TestService");
//		new AopClassFileTransformer().transformInit("com/meizu/aop/service/TestService");
		long start = System.currentTimeMillis();
			AnnotationInfo cai = new AnnotationInfo();
			CacheDataAdd cacheDataAdd = new CacheDataAdd() {
				
				@Override
				public Class<? extends Annotation> annotationType() {
					return CacheDataAdd.class;
				}
				
				@Override
				public String value() {
					return "cachead";
				}
				
				@Override
				public String key() {
					return "cacheTest";
				}
				
				@Override
				public String condition() {
					return "";
				}
			};
			cai.setAnnotatoionType(cacheDataAdd);
			cai.setReturnType(Object.class);
			CacheAnnotationResolver.cacheAnnotationInfoMap.put("com.meizu.simplify.aop.service.TestService:doSomeThing", cai);
			 User bb = new User();
		        bb.setName("yyyyy");
//		        类加载冲突 ，后续打开 TODO
//		        TestService testService = (TestService)(cc.toClass().newInstance());
//		        testService.doSomeThing(bb);
		System.out.println(System.currentTimeMillis()-start);
	}
}
