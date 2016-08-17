package com.meizu.simplify.config.info;

import com.meizu.simplify.exception.MessageException;

/**
 * <p><b>Title:</b><i>请求响应提示信息ThreadLocal</i></p>
 * <p>Desc: 1.用于向用户呈现提示信息,主要用于页面提示信息，必须配合mvc模块才可以完成对页面的提示
 *          2.待优化，不要使用MessageException实体，要使用普通实体</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月23日 上午11:10:54</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月23日 上午11:10:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MessageThreadLocal {
    public static final ThreadLocal<MessageException> threadLocal = new ThreadLocal<>();

    public static void info(String message) {
        MessageException messageException = new MessageException(208,message);
        threadLocal.set(messageException);
    }

    /**
     *
     * 方法用途: 警告信息<br>
     * 操作步骤: TODO<br>
     * @param message 错误信息
     */
    public static void warn(String message) {
        MessageException messageException = new MessageException(300,message);
        threadLocal.set(messageException);
    }
    /**
     *
     * 方法用途: 警告信息<br>
     * 操作步骤: TODO<br>
     * @param message 错误信息
     */
    public static void error(String message) {
        MessageException messageException = new MessageException(500,message);
        threadLocal.set(messageException);
    }
    
    /**
    *
    * 方法用途: 警告信息<br>
    * 操作步骤: 暂未实现 TODO<br>
    * @param message 错误信息
    */
   /*public static void error(Object message) {
	   
   }*/
}
