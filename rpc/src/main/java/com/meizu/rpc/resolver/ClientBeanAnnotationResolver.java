package com.meizu.rpc.resolver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.meizu.rpc.annotations.ClientBean;
import com.meizu.rpc.annotations.ServerBean;
import com.meizu.simplify.Constants;
import com.meizu.simplify.exception.StartupErrorException;
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
public class ClientBeanAnnotationResolver implements IBeanHook ,AutoCloseable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBeanAnnotationResolver.class);
	private final ConcurrentMap<String, ReferenceConfig<?>> referenceConfigs = new ConcurrentHashMap<String, ReferenceConfig<?>>();
	
	@Override
	public BeanEntity<?> hook(Class<?> clazz){
		List<Class<?>> entityClasses = ClassUtil.findClassesByAnnotationClass(ClientBean.class, Constants.packagePrefix);//扫描ClientBean注解bean
		if (CollectionUtil.isNotEmpty(entityClasses)) {
			for (Class<?> entityClass : entityClasses) {
				if(!clazz.getName().equals(entityClass.getName())){
					continue;
				}
				List<Class<?>> allIpmlClass=ClassUtil.findClassesByParentClass(entityClass, Constants.packagePrefix);
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
//			String group=propertieUtil.getString("dubbo.registry.group");
			String key =  entityClass.getName() + ":" + beanAnnotation.version();
			ReferenceConfig<?> reference =referenceConfigs.get(key);
			if (reference == null) {
				ApplicationConfig application = new ApplicationConfig();
				application.setName(propertieUtil.getString("dubbo.application.name"));
				// 连接注册中心配置
				RegistryConfig registry = new RegistryConfig();
				registry.setAddress(propertieUtil.getString("dubbo.registry.address"));
//				registry.setGroup(group);
//				registry.setProtocol(propertieUtil.getString("dubbo.protocol.name"));
				// 引用远程服务
				reference = new ReferenceConfig<>();
				reference.setApplication(application);
				reference.setRegistry(registry);
				reference.setProtocol(propertieUtil.getString("dubbo.protocol.name"));
//				reference.setRegistries(this.buildRegistryAdress(propertieUtil));//多注册中心
				reference.setInterface(entityClass);
				reference.setVersion(beanAnnotation.version());
				reference.setCheck(beanAnnotation.check());
				reference.setUrl(beanAnnotation.url());
				String monitorPro=propertieUtil.getString("dubbo.monitor.protocol");
				if (StringUtil.isNotBlank(monitorPro)) {
					MonitorConfig monitor = new MonitorConfig();// 监控
					monitor.setProtocol(monitorPro);
					reference.setMonitor(monitor);
				}
			}
			BeanEntity<Object> resultEntity = new BeanEntity<Object>();
			Object obj=reference.get();
			resultEntity.setName(entityClass.getName());
			resultEntity.setBeanObj(obj);
			referenceConfigs.putIfAbsent(key, reference);
			return resultEntity;
		}catch(Exception e){
			LOGGER.error("连接dubbo服务异常！请检查"+entityClass.getName()+"服务是否启用！"+e.getMessage());
			throw new StartupErrorException("连接dubbo服务异常！请检查"+entityClass.getName()+"服务是否启用！"+e.getMessage());
		}finally{
			
		}
	}
//	private List<RegistryConfig> buildRegistryAdress(PropertieUtil propertieUtil){
//		String addresss = propertieUtil.getString("dubbo.registry.address");
//		List<RegistryConfig> registries=new ArrayList<RegistryConfig>();
//		if (StringUtil.isNotBlank(addresss)) {
//			String[] addressArry = addresss.split(",");
//			for (int i = 0; i < addressArry.length; i++) {
//				if(StringUtil.isBlank(addressArry[i]))continue;
//				RegistryConfig registry = new RegistryConfig();
//				registry.setAddress(addressArry[i]);
//				registries.add(registry);
//			}
//		}
//		return registries;
//	}
	@Override
	public void close() throws IOException {
		for (ReferenceConfig<?> referenceConfig : referenceConfigs.values()) {
            try {
            	LOGGER.info(referenceConfig+" destroy start..........");
                referenceConfig.destroy();
            } catch (Throwable e) {
            	LOGGER.error(referenceConfig+" destroy exception"+e.getMessage(), e);
            }
        }
	}
}
