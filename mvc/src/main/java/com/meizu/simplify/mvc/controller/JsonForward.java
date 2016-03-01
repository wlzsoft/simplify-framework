package com.meizu.simplify.mvc.controller;

import com.meizu.simplify.utils.JsonUtil;


/**
 * <p><b>Title:</b><i>json处理返回方式</i></p>
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
public  class  JsonForward extends MessageForward {

	public JsonForward(Object obj) {
		super("");
		//TODO 需要判断是否是集合，如果是集合，判断长度是否是1，如果是1，那么取出来，转成单一一个对象，再转成json
		String message = JsonUtil.ObjectToJson(obj);
		super.setMsg(message);
	}
}
