package com.meizu.simplify.aop.resolver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.Constants;
import com.meizu.simplify.aop.AopClassFileTransformer;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;

import javassist.CtClass;

/**
  * <p><b>Title:</b><i>织入解析器</i></p>
 * <p>Desc: 用于替代javaagent的Instrumentation的方式</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.WEAVING)
public class WeavingAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(WeavingAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		AopClassFileTransformer aft = new AopClassFileTransformer();
		List<Class<?>> classList = ClassUtil.findClasses(Constants.packagePrefix+".simplify.aop",Constants.packagePrefix+".simplify.utils");//扫描范围过大，后续可以考虑优化，减少Class的扫描
		for (Class<?> clazz : classList) {
			String className = clazz.getName();
			try {
				CtClass ctClass = aft.pool.get(className);
				CtClass targetClassByteCode = aft.embed(className, ctClass);
				LOGGER.info("test:"+targetClassByteCode);
			} catch (Exception e) {
//			} catch (IllegalClassFormatException e) {
				e.printStackTrace();
				LOGGER.error("无效的Class格式异常:待织入的类"+className+"字节码的格式错误");
			}
		}
		AopClassFileTransformer.printAopMappingInfo();
		LOGGER.info("已启用织入功能");
	}
}
