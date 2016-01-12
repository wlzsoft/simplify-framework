package com.meizu.simplify.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * <p><b>Title:</b><i>JSON工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午3:15:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午3:15:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonUtil {
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToJson(Object obj){
    	return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }
    
	/**
	 * 字符还原成JSON
	 * @param str
	 * @return
	 */
    public static Object JsonToObject(String str){
    	return JSON.parse(str);
    }
}
