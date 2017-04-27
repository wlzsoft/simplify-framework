package vip.simplify.log.logback;

import java.net.SocketException;

import vip.simplify.utils.ip.IpUtil;

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