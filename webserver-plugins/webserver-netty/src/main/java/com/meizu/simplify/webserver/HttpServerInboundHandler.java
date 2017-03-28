package com.meizu.simplify.webserver;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.mvc.ControllerFilter;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.utils.UUIDUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;




public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {  //SimpleChannelUpstreamHandler 
    private static Logger   logger  = LoggerFactory.getLogger(HttpServerInboundHandler.class);  
    private static final ControllerFilter filter = new ControllerFilter();
    public static Map<String, HttpSessionImplWrapper> sessions = new HashMap<String, HttpSessionImplWrapper>();
    HttpResponseImpl responseImpl = null;
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
    	
    	HttpRequestImpl request = new HttpRequestImpl();
        if (msg instanceof HttpRequest) {  
            HttpRequest request2 = (HttpRequest) msg; 
            request.parseRequestLine(request2);
    		//解析请求头信息
    		/*String requestHead = null;
    		while(StringUtil.isNotBlank(requestHead = br.readLine())) {
    			System.out.println(requestHead);
    			request.parseRequestHeader(requestHead);
    		}*/
    		
    		//解析请求体信息-请求参数
    		//post方法请求的参数数据的处理
    		/*String contentLength = request.getRequestHeader().get("Content-Length");
    		System.out.println("ContentLength :" + contentLength);
    		if (contentLength != null) {
    			int length = Integer.parseInt(contentLength);
    			char[] buffer = new char[length];
    			br.read(buffer);
    			System.out.println("datas : " + new String(buffer));
    			String postData = new String(buffer);
    			String[] parameters = postData.split("&");
    			for (String str : parameters) {
    				String[] datas = str.split("=");
    				request.getParameters().put(datas[0], datas[1]);
    			}
    			request.setBody(buffer);
    		}*/
        }  
        
        
        responseImpl = new HttpResponseImpl(ctx);
        if (msg instanceof HttpContent) {  
			HttpContent httpContent = (HttpContent) msg;  
            ByteBuf buf = httpContent.content();  
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "UTF-8");
			System.out.println(body);
//			Discard the received data silently.
			httpContent.release();  
			
			//session解析处理
			String sessionId = request.getCookiesMap().get("sessionId");
			HttpSessionImplWrapper session = null;
			if(StringUtil.isBlank(sessionId)) {
				session = new HttpSessionImplWrapper();
				session.setSessionId(UUIDUtil.getRandomUUID());
				sessions.put(session.getSessionId(), session);
			} else {
				session = sessions.get(sessionId);
				if(session == null) {
					session = new HttpSessionImplWrapper();
					session.setSessionId(UUIDUtil.getRandomUUID());
					sessions.put(session.getSessionId(), session);
				}
			}
			request.setSession(session);
			// cookie的可以为set-cookie，是http协议规定的，请求头上面cookie设为sessionID
			responseImpl.getResponseHeader().put("Set-Cookie","sessionId=" + session.getSessionId());
			//执行具体业务处理--先是路由选择-路径选择-业务处理-写到缓冲中，准备发送到浏览器
			filter.doFilter(request, responseImpl, null);
			responseImpl.getWriter().flush();
        	responseImpl.getWriter().close();
			
        }  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        logger.info("HttpServerInboundHandler.channelReadComplete");  
      //把缓冲区中的内容刷到浏览器，并关闭连接
        if(responseImpl != null) {
//        	responseImpl.getWriter().flush();
//        	responseImpl.getWriter().close();
        }
        ctx.flush();
//        ctx.disconnect();
        ctx.close();
    }  
    
    @Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
	}
    
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
	}
    
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		Channel ch = ctx.channel();
		if (e instanceof TooLongFrameException) {
			sendError(ch, HttpResponseStatus.BAD_REQUEST,e);
			return;
		}

		e.printStackTrace();
		if (ch.isActive()) {
			sendError(ch, HttpResponseStatus.INTERNAL_SERVER_ERROR,e);
		}
	}

	private void sendError(Channel ch, HttpResponseStatus status,Throwable e) {
		
		String text = "Failure: " + status.toString() + "\r\n";
		/*ByteBuf byteBuf = Unpooled.buffer();
        byte[] bytes = text.getBytes("utf-8");
        byteBuf.writeBytes(bytes);*/
		ByteBuf byteBuf = Unpooled.copiedBuffer(text.toCharArray(),CharsetUtil.UTF_8);//netty3.x用的是ChannelBuffers.copiedBuffer
//		ByteBuf byteBuf = Unpooled.wrappedBuffer("I am ok".getBytes());
//		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,byteBuf);
		HttpHeaders headers = response.headers();
//		headers.add(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		headers.set(HttpHeaderNames.CACHE_CONTROL, "no-cache");
		headers.set(HttpHeaderNames.PRAGMA, "No-cache");
		headers.set(HttpHeaderNames.SERVER, "Simplify Server");
		headers.set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
		headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE); 
//		response.setDecoderResult(DecoderResult.failure(e));
		// Close the connection as soon as the error message is sent.
		ch.write(response).addListener(ChannelFutureListener.CLOSE);
//        ch.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
    
  
}  