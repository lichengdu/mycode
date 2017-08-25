package com.masget.openapi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.masget.openapi.config.HttpServerConfig;

public final class HttpServer
{

	static Log log = LogFactory.getLog(HttpServer.class);
	static boolean SSL = false;
	static int PORT = 8080;

	public static void main(String[] args) throws Exception
	{

		/* 初始化配置 */
		HttpServerConfig.getConfig().loadPropertiesFromSrc();
		SSL = HttpServerConfig.getConfig().getSsl();
		PORT = HttpServerConfig.getConfig().getPort();
		HttpServerSpringContext.getContext();

		new HttpServer().run();
	}

	public void run() throws Exception
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new HttpServerInitializer());
			Channel ch = b.bind(PORT).sync().channel();

			String httpstr = "";
			if (HttpServerConfig.getConfig().getSsl())
			{
				httpstr = "https";
			}
			else
			{
				httpstr = "http";
			}
			log.info(httpstr + " server start sucessful bind at port " + PORT + '.');
			log.info("Open your browser and navigate to " + httpstr + "://localhost:" + PORT + '/');

			ch.closeFuture().sync();
		}
		finally
		{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
