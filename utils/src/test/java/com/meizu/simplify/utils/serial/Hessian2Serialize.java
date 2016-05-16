package com.meizu.simplify.utils.serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

/**
  * <p><b>Title:</b><i>Hessian2序列化支持</i></p>
 * <p>Desc: 和resin同一公司开发的组件，对servlet容器的集成提供了支持</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月16日 下午2:20:37</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月16日 下午2:20:37</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Hessian2Serialize<T> implements ISerialize<T>{
	
	
	@Override
	public byte[] serialize(Object obj) {  
	    if(obj==null) {
	    	throw new NullPointerException();  
	    }
	    ByteArrayOutputStream os = new ByteArrayOutputStream();  
	    HessianOutput ho = new HessianOutput(os);  
	    try {
			ho.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    return os.toByteArray();  
	}  
	
	@Override	
	public T unserialize(byte[] sec) {
		if(sec==null) {
		 throw new NullPointerException();  
		}
	    ByteArrayInputStream is = new ByteArrayInputStream(sec);  
	    HessianInput hi = new HessianInput(is);  
	    try {
			Object r = hi.readObject();
			return (T) r;
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    return null;
	}  
}
