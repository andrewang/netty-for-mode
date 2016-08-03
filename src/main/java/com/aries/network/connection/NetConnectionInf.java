package com.aries.network.connection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import com.aries.network.codec.CMDCodec;

public abstract interface NetConnectionInf {
	/**
	 * 关闭连接通知，仅执行服务器自己的逻辑，<br>
	 * 不用通过ChannelHandlerContext断开连接<br>
	 * 断开连接应由handler处理
	 * @param channelId
	 */
	abstract void closeConNotify(ChannelId channelId);
	/**
	 * 注册连接通知
	 * @param connection
	 */
	abstract void registerConNotify(ChannelHandlerContext connection);
	/**
	 * 接收到消息,并分发到对应的
	 * @param codecData
	 */
	abstract void receiveMessage(ChannelId channelId,CMDCodec codecData);
}
