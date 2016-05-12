package com.meizu.simplify.template;

import org.beetl.core.Resource;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月12日 上午10:53:29</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月12日 上午10:53:29</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeetlClasspathResourceLoader extends ClasspathResourceLoader {

	@Override
	public Resource getResource(String key) {
		Resource resource = new BeetlClasspathResource(key, super.getRoot() + key,this);
		return resource;
	}

}
