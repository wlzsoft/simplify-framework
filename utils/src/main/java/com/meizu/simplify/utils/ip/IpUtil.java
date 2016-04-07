package com.meizu.simplify.utils.ip;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年8月6日 下午7:43:40</p>
 * <p>Modified By:whb-</p>
 * <p>Modified Date:2015年8月6日 下午7:43:40</p>
 * @author <a href="mailto:wanghaibin@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class IpUtil {

	/**
	 * 只取ipv4且如有多个ip,只取第一个
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getLocalIp() throws SocketException {

		Enumeration e1 = (Enumeration) NetworkInterface.getNetworkInterfaces();
		String ip = "";
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			Enumeration e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue; // ignore ipv6
				if (!ia.isLoopbackAddress()
						&& ia.getHostAddress().indexOf(":") == -1) {
					ip = ia.getHostAddress();
				}
			}
		}

		return ip;
	}
}
