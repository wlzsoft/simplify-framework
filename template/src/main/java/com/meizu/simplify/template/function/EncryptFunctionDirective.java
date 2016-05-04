package com.meizu.simplify.template.function;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;


/**
 * <p><b>Title:</b><i>加密函数</i></p>
 * <p>Desc: 基于velocity的功能指令</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:24:44</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:24:44</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class EncryptFunctionDirective extends Directive {

	@Override
	public String getName() {
		return "encrypt";
	}
	
	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		SimpleNode snType = (SimpleNode) node.jjtGetChild(0);
		String type = (String) snType.value(context);

		SimpleNode snValue = (SimpleNode) node.jjtGetChild(1);
		String value = (String) snValue.value(context);

		String key = "";
		SimpleNode snKey = (SimpleNode) node.jjtGetChild(2);
		if (snKey != null) {
			key = (String) snKey.value(context);
		}

	/*	if ("md5".equals(type)) {
			writer.write(MD5Encrypt.sign(value));
		} else if ("e_base64".equals(type)) {
			writer.write(Base64Encrypt.encode(value, key));
		} else if ("d_base64".equals(type)) {
			writer.write(Base64Encrypt.decode(value, key));
		} else if ("e_des".equals(type)) {
			writer.write(DESEncrypt.encrypt(value, key));
		} else if ("d_des".equals(type)) {
			writer.write(DESEncrypt.decrypt(value, key));
		}*/
		return true;
	}

}
