package com.meizu.data.mybatis.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.StringUtil;


/**
 * <p><b>Title:</b><i>where条件实体数据封装传输类</i></p>
 * <p>Desc: TODO 解决sql注入问题。主要针对无法进行预处理的情况。通用方法的处理 ：三、通过限定特定字符解决（方法三局限于特定的公用方法，针对WhereDTO实体，比如opeator属性只支持=<>like）等字符。xss补充，lexer技术来过滤，而不用正值表达式。</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月18日 上午10:21:10</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月18日 上午10:21:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class WhereDTO implements Serializable {
	private static final long serialVersionUID = -8339141624984065226L;
	private static final Map<String,String> opertorMap = new HashMap<>();
	static {
		opertorMap.put("=", "true");
		opertorMap.put(">=", "true");
		opertorMap.put("<=", "true");
		opertorMap.put(">", "true");
		opertorMap.put("<", "true");
		opertorMap.put("like", "true");
		opertorMap.put("or", "true");
		opertorMap.put("and", "true");
		opertorMap.put("in", "true");
		opertorMap.put("not in", "true");
	}
	/**
	 * where条件中的字段名
	 */
	private String key;
	/**
	 * where操作符
	 */
	private String operator;
	
	/**
	 * where条件中的值:1.普通类型，单个值，
	 *             2.特殊类型比如(x,x,x,x) 等等。
	 */
	private Object value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		if(StringUtil.isEmpty(key)) {
			throw new UncheckedException("非法操作！");
		}
		if(key.contains("select")||key.contains("delete")||key.contains("drop")||key.contains("from")||key.contains("where")) {
			throw new UncheckedException("非法操作！");
		}
		this.key = key;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		if(StringUtil.isEmpty(operator)) {
			throw new UncheckedException("非法操作！");
		}
		String opertorValue = opertorMap.get(operator.trim());
		if(opertorValue == null) {
			throw new UncheckedException("非法操作！");
		}
		this.operator = operator;
	}
	public Object getValue() {
		if("like".equals(operator)) {
			return value+"%";
		}
		return value;
	}
	public void setValue(Object value) {
		if(StringUtil.isEmpty(key)) {
			throw new UncheckedException("非法操作！");
		}
		if(key.contains("select")||key.contains("delete")||key.contains("drop")||key.contains("from")||key.contains("where")) {
			throw new UncheckedException("非法操作！");
		}
		this.value = value;
	}
}
