package vip.simplify.ioc.service;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.enums.BeanTypeEnum;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午3:15:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午3:15:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean(type= BeanTypeEnum.SINGLE)
public class DemoService implements IDemoService{
	private String name;
	@Override
	public String getName() {
		return this+name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}
