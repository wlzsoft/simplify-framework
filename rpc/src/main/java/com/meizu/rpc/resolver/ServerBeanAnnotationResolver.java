package com.meizu.rpc.resolver;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.ServiceConfig;
import com.meizu.rpc.annotations.ServerBean;
import com.meizu.rpc.config.DubboApplication;
import com.meizu.rpc.config.DubboMonitor;
import com.meizu.rpc.config.DubboPropertiesConfig;
import com.meizu.rpc.config.DubboProtocol;
import com.meizu.rpc.config.DubboRegistry;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>RPC服务注解解析器</i></p>
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
@Init(InitTypeEnum.SERVER_BEAN)
public class ServerBeanAnnotationResolver implements IAnnotationResolver<Class<?>> ,Closeable{

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerBeanAnnotationResolver.class);
	
	 private final Set<ServiceConfig<?>> serviceConfigs = new ConcurrentHashSet<ServiceConfig<?>>();
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		buildAnnotation(ServerBean.class);
	}

	public  <T extends ServerBean> void buildAnnotation(Class<T> clazzAnno) {
		DubboApplication application = BeanFactory.getBean(DubboApplication.class);
 		DubboProtocol protocol = BeanFactory.getBean(DubboProtocol.class);
		DubboRegistry registry = BeanFactory.getBean(DubboRegistry.class);
		DubboMonitor monitor = BeanFactory.getBean(DubboMonitor.class);
		DubboPropertiesConfig dubboProperties=BeanFactory.getBean(DubboPropertiesConfig.class);;
//		List<DubboRegistry> registryList=new ArrayList<DubboRegistry>();
//		List<Object> beanList=new ArrayList<Object>();
		Set<Entry<String, Object>>  resoveBean = BeanFactory.getBeanContainer().getMapContainer().entrySet();
		String loadbalanceConfig=dubboProperties.getProp().getString("dubbo.service.loadbalance");
		for (Entry<String, Object> clazzObj : resoveBean) {
			Object bean = clazzObj.getValue();
			Class<?> clazz = bean.getClass();
//			if(clazz.equals(DubboRegistry.class)){
//				registryList.add((DubboRegistry) bean);
//			}
			try {
				ServerBean beanAnnotation = clazz.getAnnotation(ServerBean.class);
				if(beanAnnotation == null) {
					continue;
				}
				LOGGER.info("dubbo服务 初始化:{}", clazz.getClass().getName());
				Class<?> interfaces=clazz.getInterfaces()[0];
				ServiceConfig<Object> service = new ServiceConfig<Object>(); 
				service.setApplication(application);
				service.setRegistry(registry);
				service.setMonitor(monitor);
				service.setProtocol(protocol);
				service.setInterface(interfaces);
				service.setTimeout(beanAnnotation.timeout());
//				String loadbalance=beanAnnotation.loadbalance().getValue();
				service.setLoadbalance(loadbalanceConfig);
				service.setRef(bean);
				service.setVersion(beanAnnotation.version());
				serviceConfigs.add(service);
				service.export();
			} catch (Exception e) {
				LOGGER.error("dubbo服务:" + clazz.getName() + "初始化失败" + e);
				throw new StartupErrorException("注册dubbo"+clazz.getName()+"服务异常"+e.getMessage());
			}

		}
		
	}

	@Override
	public void close() throws IOException {
		for (ServiceConfig<?> serviceConfig : serviceConfigs) {
			try {
				LOGGER.info(serviceConfig+" unexport start........");
				serviceConfig.unexport();
			} catch (Throwable e) {
				LOGGER.error(e.getMessage()+serviceConfig+" unexport exception", e);
			}
		}
	}
	
}
