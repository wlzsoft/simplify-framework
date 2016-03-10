package com.meizu.simplify.cache.redis.util;

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
	 * 方法用途: JSON转换成字符串-带元数据信息<br>
	 * 操作步骤: 注意：会有的@type用来表示类型,尽量避免使用
	 *           建议使用场景，容器启动时，不建议用于网络传输中<br>
	 * @param obj
	 * @return
	 */
	public static String ObjectToJsonAndContainMeta(Object obj){
    	return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }
	
	/**
	 * 
	 * 方法用途: JSON转换成字符串-不带元数据信息<br>
	 * 操作步骤: TODO <br>
	 * @param obj
	 * @return
	 */
	public static String ObjectToJson(Object obj){
    	return JSON.toJSONString(obj,new JsonAfterFilter());
    }
    
	/**
	 * 方法用途: 字符还原成JSON<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
    public static Object JsonToObject(String str){
    	return JSON.parse(str);
    }
}
