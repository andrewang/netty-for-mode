package com.aries.network.connection;

import io.netty.channel.ChannelHandlerContext;

import com.aries.network.codec.CMDCodec;

public abstract interface ConnectionInf {
	/**
	 * 发送 CMDCodec  消息给客户端
	 * @param codecData
	 */
	abstract void sendCommand(CMDCodec codecData);
	/**
	 * 获取远程连接的ip
	 * @return
	 */
	abstract String getRemoteIPAndPort();
	/**
	 * 获取连接上下文
	 * @return
	 */
	abstract ChannelHandlerContext getConnectionContext();
	/**
	 * 关闭该链接
	 * @return
	 */
	abstract void closeConnection();
}
