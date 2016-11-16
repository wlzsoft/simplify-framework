package com.meizu.simplify.log.hook;

import java.lang.reflect.Field;

import com.meizu.simplify.ioc.annotation.IocHook;
import com.meizu.simplify.ioc.hook.IIocHook;
import com.meizu.simplify.log.Logger;
/**
 * <p>Logger类型属性依赖注入钩子处理类</p>
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
@IocHook(Logger.class)
public class LogIocHook implements IIocHook{
	
	@Override
	public String hook(Class<?> clazz,Field field){
		return clazz.getName()+"Log";
	}
}
