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
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;




public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {  //SimpleChannelUpstreamHandler 
    private static Logger   logger  = LoggerFactory.getLogger(HttpServerInboundHandler.class);  
    private static final ControllerFilter filter = new ControllerFilter();
    public static Map<String, HttpSessionImplWrapper> sessions = new HashMap<String, HttpSessionImplWrapper>();
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
    	
        if (msg instanceof HttpRequest) {  
            HttpRequest request = (HttpRequest) msg; 
        }  
        
        HttpRequestImpl request = new HttpRequestImpl();
        HttpResponseImpl responseImpl = new HttpResponseImpl();
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
			
        }  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        logger.info("HttpServerInboundHandler.channelReadComplete");  
      //把缓冲区中的内容刷到浏览器，并关闭连接
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
		ctx.close();
	}

	private void sendError(Channel ch, HttpResponseStatus status,Throwable e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//		response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.setDecoderResult(DecoderResult.failure(e));
		// Close the connection as soon as the error message is sent.
		ch.write(response).addListener(ChannelFutureListener.CLOSE);
	}
    
  
}  