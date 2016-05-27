package com.meizu.simplify.mvc;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.mvc.resolver.ControllerAnnotationResolver;
import com.meizu.simplify.utils.StringUtil;




/**
 * <p><b>Title:</b><i>controller过滤器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:15</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@WebFilter(urlPatterns="/*",dispatcherTypes={DispatcherType.REQUEST},filterName="ControllerFilter")
public class ControllerFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerFilter.class);
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String requestUrl = request.getRequestURI().substring(request.getContextPath().length());
		if(requestUrl.endsWith("/")) {//修复url最后地址为"/"符号的情况
			requestUrl = requestUrl.substring(0, requestUrl.length()-1);
		}
		if( requestUrl.indexOf("//") != -1 ) {// 防止用户避过正则
			requestUrl = requestUrl.replaceAll("//", "/"); 
		}
//		if (StringUtil.parseString(request.getQueryString(),"").length() > 0) {
//			thisUrl += "?" + request.getQueryString();
//	 	}
		ControllerAnnotationInfo<BaseController<?>> controllerAnnotationInfo = ControllerAnnotationResolver.controllerMap.get(requestUrl);
		if(controllerAnnotationInfo !=null) {
			analysisAndProcess(request, response, requestUrl, controllerAnnotationInfo, null);
			return;// [标示]启用原生 filter的chain,三个地方同时打开注释
		} else {
			//注意：性能问题1：由于正则无法确定具体值的访问，所以也没法比较，为了减少循环次数，所以采用空间换时间的方式，分开两个url映射缓存的map，一个用于存储非正则表达式(采用ConcurrentHashMap)，另外一个用于存储正则表达式(采用遍历速度更快的ConcurrentSkipListMap结构)
			//      性能问题2: 由于正则表达式解析性能很差，解决方案A.使用普通方式匹配参数路径。解决方案B.缓存正则表达式的编译过程，减少编译的成本。解决方案C.避免使用
			//      解决方案D.结合实际使用，可以第一次匹配到的路径做缓存一段时间。在这个时间内，这个路径会在指定的map中命中
			for ( Map.Entry<String,ControllerAnnotationInfo<BaseController<?>>> entrySet : ControllerAnnotationResolver.controllerRegularExpressionsList.entrySet()) {
				String key = entrySet.getKey();
				if(!key.contains("$")) {
					continue;
				}
				Pattern pattern = Pattern.compile("^" + key);
				Matcher matcher = pattern.matcher(requestUrl);
				if (matcher.find()) {
					String[] params = new String[matcher.groupCount() + 1];
					//如果有3个分组，那么执行后，会获取到4个分组，有一个总的分组，那么总的分组会做为第一个分组，也就是匹配到的总路径
					for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
					controllerAnnotationInfo = entrySet.getValue();
					analysisAndProcess(request, response, requestUrl,controllerAnnotationInfo, params);
					return; // [标示]启用原生 filter的chain,三个地方同时打开注释
				}
			}
		}
		/*注意：匹配不上正则，匹配不上action，会走filterchain,大部分情况是不会执行chain,
		如果有多个filter的情况，就要注意，如果这个地方注释掉，可能导致后续的filter不会执行
		[jsp和servlet会走这个chain，不支持直接访问servlet和jsp，可以整个屏蔽掉，jsp还是可以访问，直接会返回200的空白页]
		[会屏蔽掉资源文件的访问，比如js，png等图片等等]*/
		if(chain != null) {
			chain.doFilter(req, res);// [标示]启用原生 filter的chain,三个地方同时打开注释
		}
	}
	
	/**
	 * 
	 * 方法用途: 分析请求地址最终调用分发处理业务<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestUrl
	 * @param key
	 * @param params
	 */
	private void analysisAndProcess(HttpServletRequest request, HttpServletResponse response, String requestUrl,ControllerAnnotationInfo<BaseController<?>> controllerAnnotationInfo,
			String[] params) {
		long time = System.currentTimeMillis();
		Statistics.getReadMap().put(requestUrl, 0);
		request.setAttribute("params", params);
		String parName = controllerAnnotationInfo.getMethod();
		request.setAttribute("cmd", parName);//请求指令，其实就是controller请求方法名
		BaseController<?> bs = controllerAnnotationInfo.getObj();
		bs.process(request, response,requestUrl);
		long readtime = System.currentTimeMillis() - time;
		LOGGER.info(StringUtil.format("{0} 耗时:{1}毫秒", requestUrl, (readtime))+"sessionId:"+request.getSession().getId());
		// 记录统计信息
		Statistics.incReadcount();
		Statistics.setReadMaxTime(readtime, requestUrl);
		Statistics.getReadMap().remove(requestUrl);
	}

	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
