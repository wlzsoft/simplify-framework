
package com.meizu.simplify.webserver;

import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import java.io.IOException;

public class ServletOutputStreamImpl extends ServletOutputStream {

    private ByteBufOutputStream out;
    private ChannelHandlerContext ctx;
    private FullHttpResponse response;
    private boolean flushed = false;

    public ServletOutputStreamImpl(FullHttpResponse response,ChannelHandlerContext ctx) {
    	this.ctx = ctx;
    	this.response = response;
        this.out = new ByteBufOutputStream(response.content());
    }

    @Override
    public void write(int b) throws IOException {
        this.out.write(b);
        out.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }

    @Override
    public void write(byte[] b, int offset, int len) throws IOException {
        this.out.write(b, offset, len);
    }

    @Override
    public void flush() throws IOException {
//        this.response.setContent(out.buffer());
    	ctx.write(response);
        this.flushed = true;
    }

    public void resetBuffer() {
        this.out.buffer().clear();
    }

    public boolean isFlushed() {
        return flushed;
    }

    public int getBufferSize() {
        return this.out.buffer().capacity();
    }

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub
		
	}
}
