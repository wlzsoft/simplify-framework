package com.meizu.simplify.log.logback;

import java.net.SocketException;

import com.meizu.simplify.utils.ip.IpUtil;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class IpConverter extends ClassicConverter {  
  
    @Override  
    public String convert(ILoggingEvent event) {  
        try {
			return IpUtil.getLocalIp();
		} catch (SocketException e) {
			e.printStackTrace();
		}  
        return "";
    }  
}  