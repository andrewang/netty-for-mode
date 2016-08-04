package com.aries.network.server;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.aries.network.codec.CodeData;
import com.aries.network.common.ModeType;
import com.aries.network.connection.ConnectionInfListener;
import com.aries.network.connection.NetConnectionInf;
import com.aries.network.handler.CommandServerHandler;

/**
 * nio服务启动类
 * ClassName &NetServer
 * @author 吴向会
 * @d2016年7月28日
 */
public class NetServer implements NetConnectionInf {
	/**
	 * 服务端口
	 */
	private int port;
	/**
	 * 服务处理器模式类型
	 */
	private static int modeType=ModeType.COMMAND_TYPE_VALUE;
	/**
	 * 连接数，默认1024
	 */
	private int SO_BACKLOG=1024;
	/**
	 * 对应io操作的记录，和监听
	 */
	private ChannelFuture future;
	/**
	 * 服务管理器
	 */
	private AbstractNetManagerInf serverManager;
	/**
	 * 客户端与服务器的连接上下文
	 */
	private ConcurrentHashMap<ChannelId,ConnectionInfListener> connections=new ConcurrentHashMap<ChannelId,ConnectionInfListener>();

	/**
	 * 选择创建模式服务
	 * @param port
	 * @param modeType
	 * @param SO_BACKLOG,连接数
	 */
	public NetServer(int port,AbstractNetManagerInf serverManager,int modeType,int SO_BACKLOG){
		this.port=port;
		NetServer.modeType=modeType;
		this.SO_BACKLOG=SO_BACKLOG;
		this.serverManager=serverManager;
	}
	
	/**
	 *  默认命令模式服务
	 * @param port
	 * @param SO_BACKLOG,连接数
	 */
	public NetServer(int port,AbstractNetManagerInf serverManager,int SO_BACKLOG){
		this.port=port;
		this.SO_BACKLOG=SO_BACKLOG;
		this.serverManager=serverManager;
	}
	/**
	 * 默认命令模式，且连接数 1024
	 * @param port
	 */
	public NetServer(int port,AbstractNetManagerInf serverManager){
		this.port=port;
		this.serverManager=serverManager;
	}
	EventLoopGroup bossGroup; // 负责接入连接的多线程事件处理器
    EventLoopGroup workerGroup; // 负责收发消息的多线程事件处理器
	//启动server服务
	public void start(){
		bossGroup = new NioEventLoopGroup(); 
        workerGroup = new NioEventLoopGroup(); 
        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); //作为一个引导程序
            bootstrap.group(bossGroup, workerGroup);//添加连接处理器组
            bootstrap.channel(NioServerSocketChannel.class);//
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(ModeType.valueOf(modeType),getCustomModeTypeHandler());
                 }
             });
            bootstrap.option(ChannelOption.SO_BACKLOG, SO_BACKLOG); //连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, false); //长连接
            //绑定端口启动服务，并等待client连接
            future = bootstrap.bind(port).sync();
            System.out.println("Server has start on: "+port+" and modeType: "+ModeType.valueOf(getModeType()));
            //直到服务器关闭才进行下一步
            future.channel().closeFuture().sync();
        }catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("server has shutdown on "+port);
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}
	
	/**
	 * 关闭服务
	 */
	public void  shutdown(){
		  future.channel().close();
		  workerGroup.shutdownGracefully();
          bossGroup.shutdownGracefully();
	}
	/**
	 * 获取自定义模式下的handler，根据modeType 获取
	 * @return
	 */
	private ChannelInboundHandlerAdapter getCustomModeTypeHandler(){
		ChannelInboundHandlerAdapter handler =null;
		switch (modeType) {
			default:
				handler=new CommandServerHandler(this);
				break;
		}
		return handler;
	}
	/**
	 * 获取对应解码编码器的模式<br>
	 * 默认命令模式 =ModeType.COMMAND_TYPE=0
	 * @return
	 */
	public static int getModeType() {
		return modeType;
	}
	public void closeConNotify(ChannelId channelId) {
		for(ChannelId id:connections.keySet()){
			System.out.println("befor: "+id);
		}
		ConnectionInfListener nConnection =connections.remove(channelId);
		if(nConnection!=null){
			nConnection.onCloseConnection();
		}
		for(ChannelId id:connections.keySet()){
			System.out.println("after: "+id);
		}
	}

	public void registerConNotify(ChannelHandlerContext connection) {
		ConnectionInfListener nConnection =serverManager.registerConNotify(connection);
		if(nConnection!=null){
			connections.put(connection.channel().id(), nConnection);
		}
	}
	
	public void receiveMessage(ChannelId channelId, CodeData codecData) {
		ConnectionInfListener nConnection =connections.get(channelId);
		if(nConnection!=null){
			nConnection.onReceiveMessage(codecData);
		}
	}
}
