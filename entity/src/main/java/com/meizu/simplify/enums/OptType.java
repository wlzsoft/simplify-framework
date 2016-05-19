package com.meizu.simplify.enums;

/**
 * 
 * <p><b>Title:</b><i>操作类型</i></p>
 * <p>Desc: </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月30日 下午5:29:07</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月30日 下午5:29:07</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 3.0
 *
 */
public enum OptType {
	/**
	 * 添加
	 */
	ADD,
	/**
	 * 修改
	 */
	MOD,
	/**
	 * 删除
	 */
	DEL,
	/**
	 * 执行
	 */
	EXEC,
	/**
	 * 查询--一般查询是不需要记录的，除非特殊情况
	 */
	SELECT;
}
