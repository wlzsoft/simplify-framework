package com.meizu.message.ribbtmq;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.meizu.simplify.entity.log.Log4JEntity;
import com.meizu.simplify.utils.DateUtil;
import com.meizu.simplify.utils.enums.DateFormatEnum;
import com.meizu.simplify.utils.ip.IpUtil;

/**
 * 自定义appender返回格式化
 * @author wanghb 
 * @Company:meizu
 * @version V1.0
 * @Date 2015年12月25日
 * @Copyright:Copyright(c)2015
 */
public class JSONLayout extends Layout {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public String format(LoggingEvent loggingEvent) {
		Log4JEntity entity=new Log4JEntity();
		try {
			writeBasic(entity, loggingEvent);
			writeThrowable(entity, loggingEvent);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return JSON.toJSONString(entity);
	}

	protected void writeThrowable(Log4JEntity entity, LoggingEvent event) throws JSONException {
		ThrowableInformation ti = event.getThrowableInformation();
		try{
			if (ti != null) {
				Throwable t = ti.getThrowable();
				entity.setClassName(t.getClass().getCanonicalName());
				entity.setLevel(event.getLevel().toString());
				entity.setCreateTime(DateUtil.format(System.currentTimeMillis(),DateFormatEnum.YEAR_TO_MILLISECOND));
				entity.setMessage(event.getMessage()+ t.getMessage());
//				entity.setLoggerName(event.getLoggerName());
				entity.setIp(IpUtil.getLocalIp());
				for (StackTraceElement ste : t.getStackTrace()) {
					entity.setFileName(ste.getFileName());
					entity.setMethod(ste.getMethodName());
					Integer lineNumber=ste.getLineNumber();
					entity.setLine(lineNumber.toString());
				}
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			throw new JSONException();
		}
	}

	protected void writeBasic(Log4JEntity entity, LoggingEvent event) throws Exception {
		entity.setClassName(event.categoryName);
		entity.setLevel(event.getLevel().toString());
		entity.setCreateTime(DateUtil.format(System.currentTimeMillis(),DateFormatEnum.YEAR_TO_MILLISECOND));
		entity.setMessage(event.getMessage());
//		entity.setLoggerName(event.getLoggerName());
		entity.setIp(IpUtil.getLocalIp());
		entity.setLine(event.getLocationInformation().getLineNumber());
		entity.setMethod(event.getLocationInformation().getMethodName());
	}

	@Override
	public boolean ignoresThrowable() {
		return false;
	}

	@Override
	public void activateOptions() {
	}

	public static void main(String [] arges){
		System.out.println(DateUtil.format(System.currentTimeMillis(),DateFormatEnum.YEAR_TO_MILLISECOND));
		
	}
}
