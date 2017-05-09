package vip.simplify.rpc.resolver;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import vip.simplify.dto.BeanMetaDTO;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.resolver.ClassMetaResolver;
import vip.simplify.rpc.annotations.ClientBean;
import vip.simplify.Constants;
import vip.simplify.exception.StartupErrorException;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.annotation.BeanHook;
import vip.simplify.ioc.hook.IBeanHook;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.rpc.config.DubboPropertiesConfig;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.utils.clazz.ClassInfo;

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
	private static final ConcurrentMap<String, ReferenceConfig<?>> referenceConfigs = new ConcurrentHashMap<>();
	
	@Override
	public BeanEntity<?> hook(Class<?> clazz) {
		List<Class<?>> allIpmlClass = ClassUtil.findClassesByParentClass(clazz, Constants.packagePrefix);
		if (CollectionUtil.isNotEmpty(allIpmlClass)) {
			return null;
		}
		ClientBean beanAnnotation = clazz.getAnnotation(ClientBean.class);
		return addRemoteBean(clazz,beanAnnotation.version(),beanAnnotation.check(),beanAnnotation.url());
	}
	
	public static BeanEntity<?> addRemoteBean(Class<?> clientBeanClass,String version,boolean check,String url) {
		String clientBeanName = clientBeanClass.getName();
		try{
			PropertieUtil propertieUtil=new PropertieUtil("properties/dubbo.properties");
//			String group=propertieUtil.getString("dubbo.registry.group");
			String key =  clientBeanName + ":" + version;
			ReferenceConfig<?> reference =referenceConfigs.get(key);
			if (reference == null) {
				ApplicationConfig application = new ApplicationConfig();
				application.setName(propertieUtil.getString("dubbo.application.name"));
				// 连接注册中心配置
				RegistryConfig registry = new RegistryConfig();
				registry.setAddress(propertieUtil.getString("dubbo.registry.address"));
				String filePath=propertieUtil.getString("dubbo.registry.file");
				if (StringUtil.isNotBlank(filePath)) {
					registry.setFile(System.getProperty("user.home")+filePath);
				}
//				registry.setGroup(group);
//				registry.setProtocol(propertieUtil.getString("dubbo.protocol.name"));
				// 引用远程服务
				reference = new ReferenceConfig<>();
				reference.setApplication(application);
				reference.setRegistry(registry);
				reference.setProtocol(propertieUtil.getString("dubbo.protocol.name"));
//				reference.setRegistries(this.buildRegistryAdress(propertieUtil));//多注册中心
				reference.setInterface(clientBeanClass);
				reference.setVersion(version);
				reference.setCheck(check);
				if (StringUtil.isNotBlank(url)) {
					reference.setUrl(url);
				}
				reference.setRetries(0);
				String monitorPro=propertieUtil.getString("dubbo.monitor.protocol");
				if (StringUtil.isNotBlank(monitorPro)) {
					MonitorConfig monitor = new MonitorConfig();// 监控
					monitor.setProtocol(monitorPro);
					reference.setMonitor(monitor);
				}
			}
			BeanEntity<Object> resultEntity = new BeanEntity<>();
			Object obj=reference.get();
			resultEntity.setName(clientBeanName);
			resultEntity.setBeanObj(obj);
			referenceConfigs.putIfAbsent(key, reference);
			return resultEntity;
		} catch(Exception e) {
			LOGGER.error("连接dubbo服务异常！请检查"+clientBeanName+"服务是否启用！"+e.getMessage());
			throw new StartupErrorException("连接dubbo服务异常！请检查"+clientBeanName+"服务是否启用！"+e.getMessage());
		}
	}
	
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
