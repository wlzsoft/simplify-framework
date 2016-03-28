package com.meizu.simplify.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.Reload;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>配置信息热加载和信息提示</i></p>
 * <p>Desc: 信息提示，通过异常机制，这块可以信息提示和信息热加载做分离，不要放在这一个类中</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月2日 下午4:56:35</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月2日 下午4:56:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Bean
//@ReloadableResource
public class Message {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);
	private static final String MESSAGE_DIR = "/properties/";
	private static final String MESSAGE_PATH = "classpath*:" + MESSAGE_DIR+ "*messages.xml";

	public Message() {
		setBasenames(MESSAGE_PATH);
	}

	/**
	 * 获取指定编码的信息
	 * 
	 * @param code 信息编码
	 * @param vars 信息变量
	 * @return 返回指定编码的信息。
	 */
	public String get(String code, Object... vars) {
		return null;
	}

	/**
	 * 抛出业务异常
	 * 
	 * @param code 信息编码
	 * @param vars 信息变量
	 */
	public void thrown(String code, Object... vars) {
		throw new UncheckedException(get(code, vars));
	}

	/**
	 * 抛出业务异常
	 * 
	 * @param ex 异常对象
	 * @param code 信息编码
	 * @param vars 信息变量
	 */
	public void thrown(Throwable ex, String code, Object... vars) {
		throw new UncheckedException(get(code, vars));
	}

//	@Reload
	public void setBasenames(String... basenames) {
		List<String> resourceBasenames = getResList(MESSAGE_DIR, basenames);
		LOGGER.info("加载配置信息文件" + resourceBasenames + "成功。");
	}
	
	/**
	 * 方法用途: 根据通配符资源路径获取资源路径列表<br>
	 * 操作步骤: TODO<br>
	 * @param resourceDir  资源目录
	 * @param wildcardResPaths   通配符资源路径
	 * @return 返回实际匹配的资源路径列表
	 */
	public static List<String> getResList(String resourceDir,
			String... wildcardResPaths) {
		List<String> resourcePaths = new ArrayList<String>();
		try {
			for (Res resource : getRessByWildcard(wildcardResPaths)) {
				String uri = resource.getURI().toString();
				String resourcePath = "classpath:" + resourceDir+ StringUtil.substringAfter(uri, resourceDir);
				resourcePaths.add(StringUtil.substringBeforeLast(resourcePath,"."));
			}
		} catch (Exception e) {
			throw new UncheckedException("获取资源文件时发生异常。", e);
		}
		return resourcePaths;
	}

	private static List<Res> getRessByWildcard(String[] wildcardResPaths) {
		// TODO Auto-generated method stub
		return null;
	}
	public class Res {

		public Object getURI() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
