package com.meizu.simplify.webcache.web;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i> 文件模式缓存</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:40</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class FileCache implements Cache {
	
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	
	
	@Override
	public String readCache(WebCache webCache, String staticName) {
		Object[] values = CacheBase.urlPageCacheMap.get(staticName);
		long time = values != null ? System.currentTimeMillis() - Long.valueOf(values[1].toString()) : -1;
		if (time > 0 && time < webCache.timeToLiveSeconds() * 1000) {
			File directory = new File(ClassUtil.getClassPath());
//			File directory = new File(config.getFileCachePath());
			try {
				FileReader fr = new FileReader(directory.getParent() + "/htmlCache/" + staticName);
				int ch = 0;
				StringBuffer sbs = new StringBuffer();
				while ((ch = fr.read()) != -1) sbs.append((char) ch);
				fr.close();
				return sbs.toString();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public boolean setCache(WebCache webCache, String staticName, String content) {
		try{
			File htmlCache = new File(config.getFileCachePath());
			if (!htmlCache.exists()) {
				htmlCache.mkdirs();
			}
			FileWriter fw = new FileWriter(htmlCache.getPath() + "/" + staticName);
			fw.write(content);
			fw.close();
			CacheBase.urlPageCacheMap.put(staticName, new Object[] { "", System.currentTimeMillis() });
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
}
