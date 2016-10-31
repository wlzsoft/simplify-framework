package com.meizu.simplify.dao;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.aop.Context;
import com.meizu.simplify.aop.Handler;
import com.meizu.simplify.aop.IInterceptor;
import com.meizu.simplify.aop.enums.ContextTypeEnum;
import com.meizu.simplify.dao.annotations.Transation;
import com.meizu.simplify.dao.datasource.ConnectionFactory;
import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.dao.resolver.TransationAnnotationResolver;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.ioc.annotation.Bean;

/**
 * <p><b>Title:</b><i>事务处理拦截器</i></p>
 * <p>Desc: 缺少对rollback的处理，这块在异常处理器中去执行</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TransationInterceptor extends Handler implements  IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(TransationInterceptor.class);
	
	@Override
	public boolean before(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
//		Object o = context.getThiz();
		
		Map<String,AnnotationInfo<Transation>> annotationInfoMap = TransationAnnotationResolver.transAnnotationInfoMap;
		AnnotationInfo<Transation> annoInfo = annotationInfoMap.get(methodFullName);
		if(annoInfo == null) {
			return false;
		}
		Annotation anno = annoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(Transation.class)) {
			DruidPoolFactory.startTransaction();
			LOGGER.debug("事务切面切入：["+methodFullName+"]方法之前 切入");
		}
		
		return true;
	}
	
	@Override
	public boolean after(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
//		Object o = context.getThiz();
		
		Map<String,AnnotationInfo<Transation>> annotationInfoMap = TransationAnnotationResolver.transAnnotationInfoMap;
		AnnotationInfo<Transation> annoInfo = annotationInfoMap.get(methodFullName);
		if(annoInfo == null) {
			return false;
		}
		Annotation anno = annoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(Transation.class)) {
			ConnectionFactory.commit();
			ConnectionFactory.close();
			LOGGER.debug("事务切面切入：["+methodFullName+"]方法之后切入");
		}
		return true;
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else {
			after(context,obj);
		}
		return true;
	}

	
	
}
