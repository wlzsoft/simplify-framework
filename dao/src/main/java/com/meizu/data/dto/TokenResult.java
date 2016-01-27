package com.meizu.data.dto;

import com.meizu.data.util.ResourceUtil;

/**
 * 
 * <p><b>Title:</b><i>文件断点续传Token对象</i></p>
 * <p>Desc: 文件断点续传Token对象</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:56:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:56:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class TokenResult extends Result{
	
	private String token;
	private long start;

	public TokenResult(String message) {
		super(message);
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public void setServer(String crossServer) {
		// TODO Auto-generated method stub
		
	}

	public void setStart(long start) {
		this.start = start;
	}
	
	public long getStart() {
		return start;
	}
}
