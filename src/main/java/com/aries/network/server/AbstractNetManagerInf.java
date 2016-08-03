package com.aries.network.server;


import com.aries.network.connection.ConnectionInfListener;

import io.netty.channel.ChannelHandlerContext;
/**
 * 服务管理器接口
 * ClassName &ServerManagerInterface
 * @author 吴向会
 * @d2016年8月1日
 */
public abstract interface AbstractNetManagerInf {
	/**
	 * 注册连接通知
	 * @param connection
	 */
	abstract ConnectionInfListener registerConNotify(ChannelHandlerContext connection);
}
