package com.meizu.rpc.hook;
/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
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

@BeanHook(ClientBean.class)
public class DubboHook implements IBeanHook {

	private static final Logger LOGGER = LoggerFactory.getLogger(DubboHook.class);
			
	@Override
	public BeanEntity<?> hook(Class<?> clazz) {
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
			RegistryConfig registry = new RegistryConfig();
			registry.setAddress(propertieUtil.getString("dubbo.registry.address"));
			// 引用远程服务
			ReferenceConfig<Object> reference = new ReferenceConfig<>();
			reference.setApplication(application);
			reference.setRegistry(registry);
			reference.setInterface(entityClass);
			reference.setVersion(beanAnnotation.version());
			BeanEntity<Object> resultEntity = new BeanEntity<>();
			Object obj=reference.get();
			resultEntity.setName(entityClass.getName());
			resultEntity.setBeanObj(obj);
			return resultEntity;
		}catch(Exception e){
			LOGGER.error("连接dubbo服务异常！"+e.getMessage());
			return null;
		}
		
	}
}
