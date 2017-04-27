package vip.simplify.cache;

import java.lang.annotation.Annotation;

import org.junit.Test;

import vip.simplify.Constants;
import vip.simplify.cache.annotation.CacheDataAdd;
import vip.simplify.cache.entity.User;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.enums.CacheFormatEnum;
import vip.simplify.cache.resolver.CacheAnnotationResolver;
import vip.simplify.dto.AnnotationInfo;

import javassist.CannotCompileException;

public class CacheSimpleTest {

	@Test
	public void test() throws InstantiationException, IllegalAccessException, CannotCompileException {
//		CtClass cc = new AopClassFileTransformer().buildClazz("vip/simplify/aop/service/TestService");
//		new AopClassFileTransformer().transformInit("vip/simplify/aop/service/TestService");
		long start = System.currentTimeMillis();
			AnnotationInfo<Annotation> cai = new AnnotationInfo<>();
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

				@Override
				public CacheFormatEnum format() {
					return CacheFormatEnum.BINARY;
				}

				@Override
				public CacheExpireTimeEnum expireTime() {
					return CacheExpireTimeEnum.CACHE_EXP_FOREVER;
				}
			};
			cai.setAnnotatoionType(cacheDataAdd);
			cai.setReturnType(Object.class);
			CacheAnnotationResolver.cacheAnnotationInfoMap.put(Constants.packagePrefix+".simplify.aop.service.TestService:doSomeThing", cai);
			 User bb = new User();
		        bb.setName("yyyyy");
//		        类加载冲突 ，后续打开 TODO
//		        TestService testService = (TestService)(cc.toClass().newInstance());
//		        testService.doSomeThing(bb);
		System.out.println(System.currentTimeMillis()-start);
	}
}
