package vip.simplify.dao.hook;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import vip.simplify.dao.orm.Dao;
import vip.simplify.ioc.annotation.IocHook;
import vip.simplify.ioc.hook.IIocHook;

/**
 * <p>Dao类型属性依赖注入钩子处理类</p>
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
@IocHook(Dao.class)
public class DaoIocHook implements IIocHook {
	
	@Override
	public String hook(Class<?> clazz,Field field){
		Type fieldType = field.getGenericType();
		if (fieldType instanceof ParameterizedType) {
            ParameterizedType parameterizedFieldType = (ParameterizedType) fieldType;
            return parameterizedFieldType.getActualTypeArguments()[0].getTypeName()+"BaseDao";
		}
		return null;
	}
}
