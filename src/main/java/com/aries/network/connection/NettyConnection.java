package com.aries.network.connection;

import com.aries.network.codec.CodeData;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class NettyConnection implements ConnectionInf{
	/**
	 * 客户端与服务气的连接上下文
	 */
	protected ChannelHandlerContext connection=null;
	public NettyConnection(ChannelHandlerContext connection){
		this.connection=connection;
	}
	public void sendCommand(CodeData codecData) {
		connection.writeAndFlush(codecData.toByteBuf());
	}
	public String getRemoteIPAndPort(){
		return connection.channel().remoteAddress().toString();
	}
	public ChannelHandlerContext getConnectionContext() {
		return this.connection;
	}
	public void closeConnection() {
		ChannelFuture future= connection.close();
		future.addListener(ChannelFutureListener.CLOSE);
	}
	
	

}
