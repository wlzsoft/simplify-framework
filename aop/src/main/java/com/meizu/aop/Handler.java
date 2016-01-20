package com.meizu.aop;

/**
 * <p><b>Title:</b><i>抽象过滤链处理器</i></p>
 * <p>Desc: 责任链方式实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午1:05:10</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午1:05:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public abstract class Handler {
	abstract public boolean handle(Object... obj);
	
	/**
	 * 下一个处理器
	 */
	private Handler handler;
	
    /**
     * 
     * 方法用途: 开始执行过滤链<br>
     * 操作步骤: TODO<br>
     * @param obj
     */
    public void invoke(Object... obj) {
    	boolean b = handle(obj);
    	if(b) {
    		next(obj);
    	}
    }

	/**
	 * 
	 * 方法用途: 设置下一个处理器<br>
	 * 操作步骤: TODO<br>
	 * @param handler
	 * @return
	 */
	protected Handler setNextHandler(Handler handler) {
		this.handler = handler;
		return handler;
	}

	/**
	 * 
	 * 方法用途: 传给下一个处理器<br>
	 * 操作步骤: TODO<br>
	 * @param obj
	 */
	protected void next(Object... obj) {
		if (handler != null) {
			handler.invoke(obj);
		} else {
			System.out.println("过滤器执行结束.");
		}
	}
}
