package vip.simplify.dao;
 
import vip.simplify.aop.Context;
import vip.simplify.aop.IInterceptor;
import vip.simplify.dao.dialect.IDialectManager;
import vip.simplify.ioc.annotation.Inject;

/**
 * 
 * <p><b>Title:</b><i>sql执行时间统计拦截器</i></p>
 * <p>Desc: 1.统计sql执行时间
 *          2.打印sql语句，同时去掉带“？”符号的预处理符号，使用真实参数，方便调试使用
 *          3.注意，如果参数为空的情况下，打印的sql语句中，会带问号，这是bug 需修复 TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月16日 上午10:24:34</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月16日 上午10:24:34</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class SqlInterceptor implements IInterceptor {
	
    @Inject
	private IDialectManager dialectManager;
    
    /*private Properties properties;
	private String dialectName;

	public void setDialectName(String dialectName) {
		this.dialectName = dialectName;
	}
	
	 public void setProperties(Properties properties) {
	        this.properties = properties;
//			String dialectClass =   properties.getProperty("dialect");   
//			
//			try {
//				dialect = (IDialect) Class.forName(dialectClass).newInstance();
//			} catch (Exception e) {
//				throw new RuntimeException(
//						"cannot create dialect instance by dialectClass:"
//								+ dialectClass, e);
//			}
	    }*/

 
   /* public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;
        long start = System.nanoTime();
        returnValue = invocation.proceed();
        long end = System.nanoTime();
        long time = (end - start);
        if (time > 1) {
            String sql = getSql(configuration, boundSql, sqlId, time);
            //System.err.println(sql);
            logger.info(sql);
        }
        return returnValue;
    }
 
    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
        String sql = showSql(configuration, boundSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(": ");
        str.append(sql);
        str.append("; time:");
        str.append(time);
        str.append("ns");
        return str.toString();
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
 
        }
        return value;
    }
 
   /* public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
 
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (int i=0; i < parameterMappings.size();i++) {
                	ParameterMapping parameterMapping = parameterMappings.get(i);
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else {
                    	sql = sql.replaceFirst("\\?", "null");
                    }
                }
            }
        }
        return sql;
    }
 
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }*/
 
    
	@Override
	public boolean before(Context context, Object... args) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean after(Context context, Object... args) {
		// TODO Auto-generated method stub
		return true;
	}
}