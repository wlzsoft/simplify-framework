package com.meizu.simplify.websocket.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.exception.CacheException;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.utils.ObjectUtil;

/**
  * <p><b>Title:</b><i>websocket请求地址解析器</i></p>
 * <p>Desc: TODO</p>
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
@Init(InitTypeEnum.WEB_SOCKET)
public class WebsocketAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketAnnotationResolver.class);
	
	public static final Map<String,AnnotationInfo<Annotation>> mappingAnnotationInfo = new ConcurrentHashMap<>();
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			Method[] methodArr = null;
			try {
				methodArr = beanClass.getDeclaredMethods();
			} catch(NoClassDefFoundError e) {
				e.printStackTrace();
				throw new CacheException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
			}
			
			for (Method method : methodArr) {
                if (method.isAnnotationPresent(RequestMap.class)) {
                	resolveAnno(beanClass, method,RequestMap.class);
                }
                if (method.isAnnotationPresent(RequestParam.class)) {
                	resolveAnno(beanClass, method,RequestParam.class);
                }
			}
		}
	}
	private <T extends Annotation> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno) {
		T requestInfo = method.getDeclaredAnnotation(clazzAnno);
		LOGGER.debug("请求映射注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
		AnnotationInfo<Annotation> cai = new AnnotationInfo<>();
		cai.setAnnotatoionType(requestInfo);
		cai.setReturnType(method.getReturnType());
		mappingAnnotationInfo.put(beanClass.getName()+":"+method.getName(), cai);
		
//		TypeVariable<?>[] tv = method.getTypeParameters();//泛型方法才会使用到
//		@RequestParam解析开始
		
		//这个参数注解的getParameterAnnotations的长度和getParameterTypes的长度相等
		Class<?>[] paramTypeClazz = method.getParameterTypes();
		Annotation[][] annotationTwoArr = method.getParameterAnnotations();
		for (int i=0; i< paramTypeClazz.length;i++) {
			Class<?> paramType = paramTypeClazz[i];
			Annotation[] annotationsParamType = annotationTwoArr[i];
			for (Annotation annotation : annotationsParamType) {//这里的循环次数是0到1次，包含@RequestParam的会循环一次，因为目前参数上只会有RequestParam注解
				if(annotation.annotationType() == RequestParam.class) {
					RequestParam param = (RequestParam)annotation;
					String paramStr = param.name();
					String exceptionMessage = beanClass.getName()+":"+method.getName()+"方法的第"+(i+1)+"个参数，参数类型为["+paramType.getName()+"]的参数名：只能是字符串，不可以是["+paramStr+"]";
					if(ObjectUtil.isInt(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的整型值");
					} else if(ObjectUtil.isBoolean(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的布尔值");
					} else if(ObjectUtil.isFloat(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的单精度浮点型值");
					} else if(ObjectUtil.isLong(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的长整型值");
					}
				}
			}
		}
//		@RequestParam解析结束
	}
}
