package com.meizu.rpc.resolver;

import java.util.List;

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
import com.meizu.simplify.utils.ClassUtil;

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
		DubboApplication application = BeanFactory.getBean("dubboApplication");
 		DubboProtocol protocol = BeanFactory.getBean("dubboProtocol");
		DubboRegistry registry = BeanFactory.getBean("dubboRegistry");
		List<Class<?>> resolveList;
		resolveList = ClassUtil.findClassesByAnnotationClass(clazzAnno, "com.meizu");
		for (Class<?> clazz : resolveList) {
			LOGGER.info("dubbo服务 初始化:{}", clazz.getName());
			try {
				ServerBean beanAnnotation = clazz.getAnnotation(clazzAnno);
				Class<?> interfaces=clazz.getInterfaces()[0];
				ServiceConfig<ServerBean> service = new ServiceConfig<ServerBean>(); 
				service.setApplication(application);
				service.setRegistry(registry); // 多个注册中心可以用setRegistries()
				service.setProtocol(protocol); // 多个协议可以用setProtocols()
				service.setInterface(interfaces.getClass());
				service.setRef(beanAnnotation);
				service.setVersion(beanAnnotation.version());
				service.export();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("dubbo服务:" + clazz.getName() + "初始化失败"+e);
			}

		}
	}
}
