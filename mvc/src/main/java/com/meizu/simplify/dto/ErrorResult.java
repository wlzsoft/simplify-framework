package com.meizu.simplify.dto;

import com.meizu.simplify.config.info.Message;

/**
 * 
 * <p><b>Title:</b><i>传输JSON对象</i></p>
 * <p>Desc: 传输JSON对象</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午2:56:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午2:56:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 */
public class ErrorResult extends Result{
	
	public ErrorResult(String message) {
		super(message);
		Message.error("500", message);
		super.setSuccess(false);
	}
	
}
