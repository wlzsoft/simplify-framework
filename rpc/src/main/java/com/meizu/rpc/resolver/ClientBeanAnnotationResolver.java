package com.meizu.rpc.resolver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.meizu.rpc.annotations.ClientBean;
import com.meizu.rpc.annotations.ServerBean;
import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.hook.IBeanHook;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;
/**
 * <p>clientBean注解解析</p>
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
@BeanHook(ClientBean.class)
public class ClientBeanAnnotationResolver implements IBeanHook {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBeanAnnotationResolver.class);
			
	@Override
	public BeanEntity<?> hook(Class<?> clazz){
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(ClientBean.class, "com.meizu");//扫描ClientBean注解bean
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {
				List<Class<?>> allIpmlClass=ClassUtil.findClassesByParentClass(entityClass, "com.meizu");
				Boolean isRemote = false;
				if (CollectionUtil.isEmpty(allIpmlClass)) {
					isRemote = true;
				} else {
					for (Class<?> implClass : allIpmlClass) {
						ServerBean serverBean = implClass.getAnnotation(ServerBean.class);
						if (null != serverBean) {
							isRemote = false;
							break;
						}
					}
				}
				if (isRemote) {
					return addRemoteBean(entityClass);
				}
			}
		}
		return null;
	}
	private BeanEntity<?> addRemoteBean(Class<?> entityClass ){
		try{
			ClientBean beanAnnotation = entityClass.getAnnotation(ClientBean.class);
			PropertieUtil propertieUtil=new PropertieUtil("properties/dubbo.properties");
			// 当前应用配置
			ApplicationConfig application = new ApplicationConfig();
			application.setName(propertieUtil.getString("dubbo.application.name"));
			// 连接注册中心配置
//			RegistryConfig registry = new RegistryConfig();
//			registry.setAddress();
			// 引用远程服务
			ReferenceConfig<Object> reference = new ReferenceConfig<>();
			reference.setApplication(application);
//			reference.setRegistry(registry);
			reference.setRegistries(this.buildRegistryAdress(propertieUtil));//多注册中心
			reference.setInterface(entityClass);
			reference.setVersion(beanAnnotation.version());
			reference.setCheck(beanAnnotation.check());
			reference.setUrl(beanAnnotation.url());
			MonitorConfig monitor=new MonitorConfig();//监控
			monitor.setProtocol("registry");
			reference.setMonitor(monitor);
			BeanEntity<Object> resultEntity = new BeanEntity<Object>();
			Object obj=reference.get();
			resultEntity.setName(entityClass.getName());
			resultEntity.setBeanObj(obj);
			return resultEntity;
		}catch(Exception e){
			LOGGER.error("连接dubbo服务异常！请检查"+entityClass.getName()+"服务是否启用！"+e.getMessage());
			return null;
		}
		
	}
	private List<RegistryConfig> buildRegistryAdress(PropertieUtil propertieUtil){
		String addresss = propertieUtil.getString("dubbo.registry.address");
		List<RegistryConfig> registries=new ArrayList<RegistryConfig>();
		if (StringUtil.isNotBlank(addresss)) {
			String[] addressArry = addresss.split(",");
			for (int i = 0; i < addressArry.length; i++) {
				if(StringUtil.isBlank(addressArry[i]))continue;
				RegistryConfig registry = new RegistryConfig();
				registry.setAddress(addressArry[i]);
				registries.add(registry);
			}
		}
		return registries;
	}
}
