
package vip.simplify.webserver;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ServletOutputStreamImpl extends ServletOutputStream {

    private ChannelHandlerContext ctx;
    private StringBuilder body;
    private HttpResponseImpl response;
    
    public ServletOutputStreamImpl(ChannelHandlerContext ctx,HttpResponseImpl response) {
    	this.ctx = ctx;
    	body = new StringBuilder();
    	this.response = response;
    }
    
    @Override
	public void print(String s) throws IOException {
    	body.append(s);
	}

    @Override
    public void flush() throws IOException {
    	DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(response.getStatus()),Unpooled.wrappedBuffer(body.toString().getBytes()));  
    	fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");  
    	fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, fullHttpResponse.content().readableBytes());  
    	fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE); 
//		fullHttpResponse.replace(Unpooled.wrappedBuffer("test".getBytes()));
    	ctx.write(fullHttpResponse);
    }

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		System.out.println("setWriteListener");
	}

	@Override
	public void write(int b) throws IOException {
		System.out.println("write int");
	}
}
