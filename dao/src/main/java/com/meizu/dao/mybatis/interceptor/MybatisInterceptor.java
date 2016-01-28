package com.meizu.dao.mybatis.interceptor;
 
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
//@Intercepts({
//        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
//        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class }) })
public class MybatisInterceptor /*implements Interceptor*/ {
	private final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties properties;
 
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
        long start = System.currentTimeMillis();
        returnValue = invocation.proceed();
        long end = System.currentTimeMillis();
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
        str.append("ms");
        return str.toString();
    }
 */
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
                for (ParameterMapping parameterMapping : parameterMappings) {
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
 
    public void setProperties(Properties properties0) {
        this.properties = properties0;
    }
}