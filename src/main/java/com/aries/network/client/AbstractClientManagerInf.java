package com.aries.network.client;

import io.netty.channel.ChannelHandlerContext;

import com.aries.network.codec.CMDCodec;
/**
 * 客户端管理器接口
 * ClassName &ClientManagerInterface
 * @author 吴向会
 * @d2016年8月1日
 */
public abstract interface AbstractClientManagerInf {
	/**
	 * 关闭连接通知，仅执行服务器自己的逻辑，<br>
	 * 不用通过ChannelHandlerContext断开连接<br>
	 * 断开连接应由handler处理
	 * @param channelId
	 */
	abstract void closeConNotify();
	/**
	 * 注册连接通知
	 * @param connection
	 */
	abstract void registerConNotify(ChannelHandlerContext connection);
	/**
	 * 接收到消息,并分发到对应的
	 * @param codecData
	 */
	abstract void receiveMessage(CMDCodec codecData);
}
