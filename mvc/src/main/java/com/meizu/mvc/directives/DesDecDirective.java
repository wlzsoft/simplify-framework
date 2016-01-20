package com.meizu.mvc.directives;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;


public class DesDecDirective extends Directive {

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
		SimpleNode sn_type = (SimpleNode) node.jjtGetChild(0);
		String type = (String) sn_type.value(context);

		SimpleNode sn_value = (SimpleNode) node.jjtGetChild(1);
		String value = (String) sn_value.value(context);

		String key = "";
		SimpleNode sn_key = (SimpleNode) node.jjtGetChild(2);
		if (sn_key != null) {
			key = (String) sn_key.value(context);
		}

		/*if ("md5".equals(type)) {
			writer.write(MD5.calcMD5(value));
		} else if ("e_base64".equals(type)) {
			writer.write(Base64.encode(value, key));
		} else if ("d_base64".equals(type)) {
			writer.write(Base64.decode(value, key));
		} else if ("e_des".equals(type)) {
			writer.write(DESStaticKey.encrypt(value, key));
		} else if ("d_des".equals(type)) {
			writer.write(DESStaticKey.decrypt(value, key));
		}*/
		return true;
	}

}
