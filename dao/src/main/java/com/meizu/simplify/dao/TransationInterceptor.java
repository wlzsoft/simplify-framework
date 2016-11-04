package com.meizu.simplify.dao;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.SQLException;
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
import com.meizu.simplify.dao.enums.ISOEnum;
import com.meizu.simplify.dao.resolver.TransationAnnotationResolver;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.ioc.annotation.Bean;

/**
 * <p><b>Title:</b><i>事务处理拦截器</i></p>
 * <p>Desc: 缺少对rollback的处理，这块在异常处理器中去执行
 *          1.目前可在mvc模块中处理异常
 *          2.TODO rpc方式的没有处理异常，后续增加（合并mvc和rpc的异常梳理，独立出异常模块）</p>
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
			Transation transation = (Transation)anno;
			Connection connection = DruidPoolFactory.getConnection();
			Integer oldTransactionISO = -1;
			try {
				oldTransactionISO = connection.getTransactionIsolation();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			LOGGER.debug("成功:事务切面切入：["+methodFullName+"]方法之前 切入,事务开启之前的隔离级别为："+oldTransactionISO);
			//明确指定可用隔离级别，并且设置的隔离级别和当前的隔离级别不同，才会设置
			if(transation.ISO() != ISOEnum.TRANSACTION_NONE && transation.ISO().getValue() != oldTransactionISO) {
				//设置隔离级别start
				if(oldTransactionISO!=-1) {
					if(connection!=null) {
						try {
							connection.setTransactionIsolation(transation.ISO().getValue());
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					context.getCallback().setTemp(oldTransactionISO);
				}
				LOGGER.debug("成功:事务切面切入：["+methodFullName+"]方法之前 切入,事务隔离级别设置为："+transation.ISO().getValue());
				//设置隔离级别end
			}
			ConnectionFactory.startTransaction(connection);
			LOGGER.debug("成功:事务切面切入：["+methodFullName+"]方法之前 切入,事务开启");
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
			Transation transation = (Transation)anno;
			Integer oldTransactionISO = context.getCallback().getTemp();
			if(transation.ISO() != ISOEnum.TRANSACTION_NONE && transation.ISO().getValue() != oldTransactionISO) {
				ConnectionFactory.commit(oldTransactionISO);
				LOGGER.debug("成功:事务切面切入：["+methodFullName+"]方法之后 切入,事务隔离级别设置还原为："+oldTransactionISO);
			} else {
				ConnectionFactory.commit();
			}
			ConnectionFactory.close();
			LOGGER.debug("成功：事务切面切入：["+methodFullName+"]方法之后切入,事务提交，并返回逻辑连接到连接池");
		}
		return true;
	}

	/**
     * 
     * 方法用途: 事务回滚处理<br>
     * 操作步骤: TODO 后续要调整这个方法的逻辑，和after 的事务提交的逻辑合并公用部分代码，并且独立出异常处理类，在方法级别处理<br>
     * @param obj
     */
	public static boolean transationRollbackResolver(String methodFullName,Integer oldTransactionISO) {
		Map<String,AnnotationInfo<Transation>> annotationInfoMap = TransationAnnotationResolver.transAnnotationInfoMap;
		AnnotationInfo<Transation> annoInfo = annotationInfoMap.get(methodFullName);
		if(annoInfo == null) {
			return false;
		}
		Annotation anno = annoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(Transation.class)) {
			Transation transation = (Transation)anno;
			if(oldTransactionISO != null && transation.ISO() != ISOEnum.TRANSACTION_NONE&&transation.ISO().getValue() != oldTransactionISO) {
				ConnectionFactory.rollback(oldTransactionISO);
				LOGGER.debug("成功:事务切面切入：["+methodFullName+"]方法之后 切入,事务隔离级别设置还原为："+oldTransactionISO);
			} else {
				ConnectionFactory.rollback();
			}
			ConnectionFactory.close();
			LOGGER.debug("成功：事务切面切入：["+methodFullName+"]方法之后切入,事务已经回滚，并返回逻辑连接到连接池");
		}
		return true;
	}
	
	@Override
	public boolean exception(Context context,Object... args) {
		Integer oldTransactionISO = null;
		if(context.getCallback()!=null) {
			oldTransactionISO = context.getCallback().getTemp();
		}
		return transationRollbackResolver(context.getMethodFullName(),oldTransactionISO);
	}
	
	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else if(context.getType().equals(ContextTypeEnum.AFTER)) {
			after(context,obj);
		} else if(context.getType().equals(ContextTypeEnum.EXCEPTION)) {
			exception(context, obj);
		} else {
			finallyer(context, obj);
		}
		return true;
	}
}
