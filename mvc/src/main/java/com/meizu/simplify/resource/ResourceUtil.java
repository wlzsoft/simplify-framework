package com.meizu.simplify.resource;

import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.StringUtil;
/**
 * 
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月5日 下午5:14:06</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月5日 下午5:14:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class ResourceUtil {
	


	/**
	 * 根据通配符资源路径获取资源路径列表。
	 * 
	 * @param resourceDir
	 *            资源目录
	 * @param wildcardResPaths
	 *            通配符资源路径
	 * @return 返回实际匹配的资源路径列表。
	 */
	public static List<String> getResList(String resourceDir,
			String... wildcardResPaths) {
		List<String> resourcePaths = new ArrayList<String>();
		try {
			for (Res resource : getRessByWildcard(wildcardResPaths)) {
				String uri = resource.getURI().toString();
					String resourcePath = "classpath:" + resourceDir
							+ StringUtil.substringAfter(uri, resourceDir);
					resourcePaths.add(StringUtil.substringBeforeLast(resourcePath,
							"."));
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

}
