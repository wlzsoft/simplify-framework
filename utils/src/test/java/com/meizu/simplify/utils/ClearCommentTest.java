package com.meizu.simplify.utils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 上午11:36:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 上午11:36:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ClearCommentTest {
	public void deepDir () {
		String rootDir = "E:\\workspace\\project\\src\\main\\java\\com";
		try {
			ClearComment.deepDir(rootDir);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
