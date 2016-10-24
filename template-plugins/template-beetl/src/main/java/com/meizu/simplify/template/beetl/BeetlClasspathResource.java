package com.meizu.simplify.template.beetl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.beetl.core.ResourceLoader;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.ClasspathResource;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月12日 上午10:38:07</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月12日 上午10:38:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BeetlClasspathResource extends ClasspathResource{
	private String path;
	private File file = null;
	long lastModified = 0;
	public BeetlClasspathResource(String key, String path, ResourceLoader resourceLoader) {
		super(key, path, resourceLoader);
		this.path = path;
	}

	@Override
	public Reader openReader() {
		InputStream is = resourceLoader.getClass().getResourceAsStream(path);
		if (is == null)	{
			BeetlException be = new BeetlException(BeetlException.TEMPLATE_LOAD_ERROR);
			be.resourceId = this.id;
			throw be;
		}
		URL url = resourceLoader.getClass().getResource(path);
		if (url != null) {
			if (url.getProtocol().equals("file"))
			{
				file = new File(url.getFile());
				lastModified = file.lastModified();
			}
		}
		Reader br;
		try
		{
			br = new BufferedReader(new InputStreamReader(is, ((ClasspathResourceLoader) this.resourceLoader).getCharset()));
			return br;
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
	
}
