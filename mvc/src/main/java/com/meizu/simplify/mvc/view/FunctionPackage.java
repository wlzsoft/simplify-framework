package com.meizu.simplify.mvc.view;

import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;

/**
 * <p><b>Title:</b><i>beetl自定义java函数</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月21日 下午2:09:42</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月21日 下午2:09:42</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class FunctionPackage {
	public String md5(String source){
		return MD5Encrypt.sign(source);
	}
}
