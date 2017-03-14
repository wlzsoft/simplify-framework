package com.meizu.simplify.webserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.plugin.annotation.Plugin;
import com.meizu.simplify.plugin.enums.PluginTypeEnum;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
  * <p><b>Title:</b><i>netty任务工厂</i></p>
 * <p>Desc: TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月29日 下午3:51:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月29日 下午3:51:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@Plugin(type=PluginTypeEnum.WEBSERVER,value="netty-http")
public class NettyHttpTaskFactory implements ITaskFactory {
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host, int port, int backlog) throws IOException {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								
								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									ByteBuf buf = (ByteBuf) msg;
									byte[] req = new byte[buf.readableBytes()];
									buf.readBytes(req);
									String body = new String(req, "UTF-8");
									System.out.println(body);
									String currentTime = "QUERY TIME ORDER";
									ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
									ctx.write(resp);
									 // Discard the received data silently.
//							        ((ByteBuf) msg).release(); 
								}
								@Override
								public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
									ctx.flush();
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
								public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
									ctx.close();
								}
							});
						}});
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		while (ServerStatus.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
