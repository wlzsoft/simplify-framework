package com.meizu.webcache.web;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.meizu.mvc.MvcInit;
import com.meizu.util.web.BrowserUtil;
import com.meizu.webcache.annotation.CacheSet;


/**
 * <p><b>Title:</b><i> 文件模式缓存</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:40</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class FileCache implements Cache {
	
	@Override
	public String readCache(CacheSet cacheSet, String staticName,Object obj) {
		Object[] values = MvcInit.urlCache.get(staticName);
		long time = values != null ? System.currentTimeMillis() - Long.valueOf(values[1].toString()) : -1;
		if (time > 0 && time < cacheSet.timeToLiveSeconds() * 1000) {
			File directory = new File(MvcInit.getPath());
			try {
				if(obj!=null&&cacheSet.enableBrowerCache()) {
					HttpServletResponse response = (HttpServletResponse) obj;
					BrowserUtil.enableBrowerCache(response,cacheSet.timeToLiveSeconds());
				}
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
	public boolean doCache(CacheSet cacheSet, String staticName, String content,Object obj) {
		try{
			if(obj!=null&&cacheSet.enableBrowerCache()) {
				HttpServletResponse response = (HttpServletResponse) obj;
				BrowserUtil.enableBrowerCache(response,cacheSet.timeToLiveSeconds());
			}
//			File directory = new File(MvcInit.getPath());
			File htmlCache = new File("D:/htmlCache/");
			if (!htmlCache.exists()) htmlCache.mkdirs();
			FileWriter fw = new FileWriter(htmlCache.getPath() + "/" + staticName);
			fw.write(content);
			fw.close();
			System.out.println(MvcInit.debug);
			MvcInit.urlCache.put(staticName, new Object[] { "", System.currentTimeMillis() });
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
}
