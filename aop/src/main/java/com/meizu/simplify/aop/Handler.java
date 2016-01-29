package com.meizu.simplify.aop;

/**
 * <p><b>Title:</b><i>���������������</i></p>
 * <p>Desc: ��������ʽʵ��</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016��1��20�� ����1:05:10</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016��1��20�� ����1:05:10</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="�����ַ">luchuangye</a>
 * @version Version 0.1
 *
 */
public abstract class Handler {
	abstract public boolean handle(Context context,Object... obj);
	
	/**
	 * ��һ��������
	 */
	private Handler handler;
	
    /**
     * 
     * ������;: ��ʼִ�й�����<br>
     * ��������: TODO<br>
     * @param obj
     */
    public void invoke(Context context,Object... obj) {
    	boolean b = handle(context,obj);
    	if(b) {
    		next(context,obj);
    	}
    }

	/**
	 * 
	 * ������;: ������һ��������<br>
	 * ��������: TODO<br>
	 * @param handler
	 * @return
	 */
	protected Handler setNextHandler(Handler handler) {
		this.handler = handler;
		return handler;
	}

	/**
	 * 
	 * ������;: ������һ��������<br>
	 * ��������: TODO<br>
	 * @param obj
	 */
	protected void next(Context context,Object... obj) {
		if (handler != null) {
			handler.invoke(context,obj);
		} else {
			System.out.println("filter end [������ִ�н���].");
		}
	}
}
