package com.meizu.simplify.mvc.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.utils.ClearCommentUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:25:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:25:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class JSPForward implements ITemplate{
	
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
		RequestDispatcher rd = request.getRequestDispatcher(templateUrl);

		// 跳转前检查静态规则
		if (webCache != null && webCache.mode() != WebCache.CacheMode.nil) {
			String content = getPageContent(request, response, rd);
			
			// 是否去除空格
			if(webCache.removeSpace()) {
				content = ClearCommentUtil.clear(content);
				content = StringUtil.removeHtmlSpace(content);
			}

			Cache cache = CacheBase.getCache(webCache);
			if(cache != null && cache.doCache(webCache, staticName, content,response)){
				// 缓存成功.
			}
			
			response.setCharacterEncoding(config.getCharset());
			response.setContentType("text/html; charset=" + config.getCharset());
			response.getWriter().print(content);

		} else {
			if(rd == null) {
				throw new UncheckedException("该容器不支持jsp视图");
			}
			rd.forward(request, response);
		}
	}

	/**
	 * 获取页面内容
	 * 
	 * @param request
	 * @param response
	 * @param rd
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private static String getPageContent(HttpServletRequest request, HttpServletResponse response, RequestDispatcher rd) throws ServletException, IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		HttpServletResponse res = new HttpServletResponseWrapper(response) {
			@Override
			public PrintWriter getWriter() {
				return pw;
			}
		};
		rd.include(request, res);
		pw.flush();
		return os.toString();
	}

}
