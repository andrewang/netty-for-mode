package com.aries.network.client;

import com.aries.network.codec.CodeData;
import com.aries.network.common.ModeType;
import com.aries.network.connection.NetConnectionInf;
import com.aries.network.handler.CommandServerHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
/**
 * 客户端网络连接器
 * ClassName &NettyClient
 * @author 吴向会
 * @d2016年8月1日
 */
public class NettyClient implements NetConnectionInf {
	/**
	 * 服务处理器模式类型
	 */
	private static int modeType = ModeType.COMMAND_TYPE_VALUE;
	/**
	 * 对应io操作的记录，和监听
	 */
	private ChannelFuture future;
	/**
	 * 服务管理器
	 */
	private AbstractClientManagerInf clientManager;
	/**
	 *  负责收发消息的多线程事件处理器
	 */
	private EventLoopGroup group=null;
	public NettyClient(AbstractClientManagerInf clientManager){
		this.clientManager=clientManager;
	}

	/**
	 * 连接io服务器
	 * @param port
	 * @param host
	 */
	public void connect(int port, String host) {
		group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(ModeType.valueOf(modeType),getCustomModeTypeHandler());
						}
					});
			// 发起异步链接操作
			future = bootstrap.connect(host, port).sync();
			System.out.println("连接服务器  IP: "+host+" Port: "+port);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 关闭，释放线程资源
			group.shutdownGracefully();
		}
	}
	/**
	 * 关闭服务
	 */
	public void  shutdown(){
		  future.channel().close();
		  group.shutdownGracefully();
	}
	/**
	 * 获取自定义模式下的handler，根据modeType 获取
	 * 
	 * @return
	 */
	private ChannelInboundHandlerAdapter getCustomModeTypeHandler() {
		ChannelInboundHandlerAdapter handler = null;
		switch (modeType) {
		default:
			handler = new CommandServerHandler(this);
			break;
		}
		return handler;
	}

	public void closeConNotify(ChannelId channelId) {
		clientManager.closeConNotify();
	}

	public void registerConNotify(ChannelHandlerContext connection) {
		clientManager.registerConNotify(connection);
	}

	public void receiveMessage(ChannelId channelId, CodeData codecData) {
		clientManager.receiveMessage(codecData);
	}
}
