package com.meizu.simplify.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.webcache.annotation.CacheSet;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class MessageForward implements IForward {
	private String msg = "";

	public MessageForward(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {
		response.setCharacterEncoding(MvcInit.charSet);
		response.setContentType("text/html; charset=" + MvcInit.charSet);
		response.getWriter().print(msg);
	}
}
