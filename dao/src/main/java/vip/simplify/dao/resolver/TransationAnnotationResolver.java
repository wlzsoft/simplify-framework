package vip.simplify.dao.resolver;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.dao.annotations.Transation;
import vip.simplify.dao.exception.DataAccessException;
import vip.simplify.dto.AnnotationInfo;
import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.util.CacheManager;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
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
@Init(InitTypeEnum.TRANS)
public class TransationAnnotationResolver implements IAnnotationResolver<Class<?>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransationAnnotationResolver.class);
	
	public static final Map<String,AnnotationInfo<Transation>> transAnnotationInfoMap = new ConcurrentHashMap<>();
	public TransationAnnotationResolver() {
		CacheManager.addCache("transAnnotationInfoMap",transAnnotationInfoMap);
	}
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
				throw new DataAccessException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
			}
			
			for (Method method : methodArr) {
                if (method.isAnnotationPresent(Transation.class)) {
                	resolveAnno(beanClass, method,Transation.class);
                }
			}
		}
	}
	private <T extends Transation> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno) {
		T transation = method.getDeclaredAnnotation(clazzAnno);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("事务注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
		}
		AnnotationInfo<Transation> cai = new AnnotationInfo<>();
		cai.setAnnotatoionType(transation);
		cai.setReturnType(method.getReturnType());
		transAnnotationInfoMap.put(beanClass.getName()+":"+method.getName(), cai);
	}
}
