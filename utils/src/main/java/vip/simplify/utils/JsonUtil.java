package vip.simplify.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import vip.simplify.exception.BaseException;

/**
 * <p><b>Title:</b><i>JSON工具类</i></p>
 * <p>Desc: 注： 后续要考虑解耦，只做工具类改有的事，想办法抽取到utils模块中,JsonAfterFilter的处理保留到缓存中 TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午3:15:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午3:15:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonUtil {
	/**
	 * 
	 * 方法用途: pojo对象转换成json字符串-带元数据信息<br>
	 * 操作步骤: 注意：会有的@type用来表示类型,尽量避免使用
	 *           建议使用场景，容器启动时，不建议用于网络传输中<br>
	 * @param obj
	 * @return
	 */
	public static String objectToStringAndContainMeta(Object obj){
    	return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }
	
	/**
	 * 方法用途: pojo对象转换成json字符串-带元数据信息<br>
	 * 操作步骤:TODO <br>
	 * @param obj
	 * @return
	 */
	public static String objectToString(Object obj,SerializerFeature...features){
    	return JSON.toJSONString(obj,features);
    }
	
	/**
	 * 
	 * 方法用途: pojo对象转换成json字符串-不带元数据信息<br>
	 * 操作步骤: 注意：这个方法后面要重构并废弃掉  因为依赖的fastJson库 需要定义出JSONFitlerFacade接口来处理，以集成其他的JSON库 TODO <br>
	 * @param obj
	 * @param afterFilter
	 * @return
	 */
	public static String objectToString(Object obj,SerializeFilter afterFilter,SerializerFeature...features){
    	return JSON.toJSONString(obj,afterFilter,features);
    }

	/**
	 *
	 * 方法用途: pojo对象转换成json字符串-不带元数据信息<br>
	 * 操作步骤: 注意：这个方法后面要重构并废弃掉  因为依赖的fastJson库 需要定义出JSONFitlerFacade接口来处理，以集成其他的JSON库 TODO <br>
	 * @param obj
	 * @param serializeConfig
	 * @return
	 */
	public static String objectToString(Object obj,SerializeConfig serializeConfig,SerializerFeature...features){
		return JSON.toJSONString(obj,serializeConfig,features);
	}
	
	/**
	 *
	 * 方法用途: pojo对象转换成json字符串-不带元数据信息<br>
	 * 操作步骤: 注意：这个方法后面要重构并废弃掉  因为依赖的fastJson库 需要定义出JSONFitlerFacade接口来处理，以集成其他的JSON库 TODO <br>
	 * @param obj
	 * @param serializeConfig
	 * @param afterFilter
	 * @return
	 */
	public static String objectToString(Object obj,SerializeConfig serializeConfig, SerializeFilter afterFilter,SerializerFeature...features){
		return JSON.toJSONString(obj,serializeConfig,afterFilter,features);
	}
    
	/**
	 * 方法用途: json字符串转对象<br>
	 * 操作步骤: 1.如果json字符串中带有类型标识，那么会转换成pojo对象
	 *           2.如果json字符串中不带有类型标识，那么会转换为内置的JSONArray和JSONObject等对象<br>
	 * @param str
	 * @return
	 */
    public static Object jsonToObject(String str){
    	return JSON.parse(str);
    }
    
    /**
	 * 方法用途: json字符串转Array对象<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
    public static JSONArray stringToJSONArray(String str){
    	return JSON.parseArray(str);
    }
    /**
	 * 方法用途: json字符串转JSONObject对象<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @return
	 */
    public static JSONObject stringToJSONObject(String str){
    	try {
    		return JSON.parseObject(str);
    	} catch(Exception ex) {
    		throw new BaseException("json解析异常，确定数据格式是否正确",ex);
    	}
    }
    
    /**
	 * 方法用途: json字符串转相应class对象实例<br>
	 * 操作步骤: TODO<br>
	 * @param str
	 * @param cls
	 * @author geny
	 * @return
	 */
    public static <T> T jsonToObject(String str, Class<T> cls){
    	return JSON.parseObject(str, cls);
    }
}
