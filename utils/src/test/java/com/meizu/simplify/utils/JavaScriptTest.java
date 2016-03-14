package com.meizu.simplify.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月14日 下午6:37:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月14日 下午6:37:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class JavaScriptTest {
	public static void main(String[] args) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager(); 
			ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn"); 
			String name = "lcy"; 
			try {
				nashorn.eval("print('" + name + "')");
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
}
