package vip.simplify.rpc.hook;

/**
 * <p>Rpc多个注册中心实例钩子</p>
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
//@BeanPrototypeHook(DubboRegistry.class)
public class DubboRegistryHook /*implements IBeanPrototypeHook*/{

//	private static final Logger LOGGER = LoggerFactory.getLogger(DubboRegistryHook.class);

	/*@Override
	public List<BeanEntity<?>> hook(Class<?> clazz) {
		List<BeanEntity<?>> list = new ArrayList<BeanEntity<?>>();
		LOGGER.debug("开始初始化Rpc多个注册中心实例....");
		PropertieUtil propertieUtil = new PropertieUtil("dubbo.properties");
		String addresss = propertieUtil.getString("dubbo.registry.address");
		if (StringUtil.isNotBlank(addresss)) {
			String[] addressArry = addresss.split(",");
			for (int i = 0; i < addressArry.length; i++) {
				DubboRegistry registry = new DubboRegistry();
				if(StringUtil.isBlank(addressArry[i]))continue;
				registry.setAddress(addressArry[i]);
				BeanEntity<Object> beanEntity = new BeanEntity<>();
				beanEntity.setName(DubboRegistry.class.getName()+i);
				beanEntity.setBeanObj(registry);
				list.add(beanEntity);
			}
		} else {
			LOGGER.error("未读取到dubbo.registry.address属性值");
		}
		return list;
	}*/
}
