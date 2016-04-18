package com.meizu.rpc.resolver;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ServiceConfig;
import com.meizu.rpc.annotations.ServerBean;
import com.meizu.rpc.config.DubboApplication;
import com.meizu.rpc.config.DubboProtocol;
import com.meizu.rpc.config.DubboRegistry;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>服务bean创建处理解析器</i></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@Init(InitTypeEnum.DUBBOBEAN)
public class DubboBeanAnnotationResolver implements IAnnotationResolver<Class<?>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(DubboBeanAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		buildAnnotation(ServerBean.class);
	}

	public static <T extends ServerBean> void buildAnnotation(Class<T> clazzAnno) {
		DubboApplication application = BeanFactory.getBean(DubboApplication.class);
 		DubboProtocol protocol = BeanFactory.getBean(DubboProtocol.class);
		DubboRegistry registry = BeanFactory.getBean(DubboRegistry.class);
		Set<Entry<String, Object>>  resoveBean = BeanFactory.getBeanContainer().getMapContainer().entrySet();
		for (Entry<String, Object> clazzObj : resoveBean) {
			Object bean = clazzObj.getValue();
			Class<?> clazz = bean.getClass();
			try {
				ServerBean beanAnnotation = clazz.getAnnotation(ServerBean.class);
				if(beanAnnotation == null) {
					continue;
				}
				LOGGER.info("dubbo服务 初始化:{}", clazz.getName());
				Class<?> interfaces=clazz.getInterfaces()[0];
				ServiceConfig<Object> service = new ServiceConfig<Object>(); 
				service.setApplication(application);
				service.setRegistry(registry); // 多个注册中心可以用setRegistries()
				service.setProtocol(protocol); // 多个协议可以用setProtocols()
				service.setInterface(interfaces);
				service.setRef(bean);
				service.setVersion(beanAnnotation.version());
				service.export();
//				test();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("dubbo服务:" + clazz.getName() + "初始化失败"+e);
			}

		}
	}
	
}
