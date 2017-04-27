package vip.simplify.webserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.plugin.annotation.Plugin;
import vip.simplify.plugin.enums.PluginTypeEnum;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;

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
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码  
							socketChannel.pipeline().addLast("httpRequestDecoder",new HttpRequestDecoder());  
							socketChannel.pipeline().addLast("httpServerCodec",new HttpServerCodec());//待确认
							/**usually we receive http message infragment,if we want full http message, 
							 * we should bundle HttpObjectAggregator and we can get FullHttpRequest
							 **/  
							//定义缓冲数据量 ，支持FullHttpRequest
			                socketChannel.pipeline().addLast("httpObjectAggregator",new HttpObjectAggregator(1024*1024*64));
			                socketChannel.pipeline().addLast(new HttpServerInboundHandler());  
			                // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
			                socketChannel.pipeline().addLast("httpResponseEncoder",new HttpResponseEncoder());  
							
							
							
							
						}}).option(ChannelOption.SO_BACKLOG, 1024) //128  
		            .childOption(ChannelOption.SO_KEEPALIVE, true); 
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
