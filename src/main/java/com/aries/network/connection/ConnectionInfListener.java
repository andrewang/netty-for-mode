package com.aries.network.connection;

import com.aries.network.codec.CMDCodec;
/**
 * 连接监听器，用于监听接收消息，和连接状态
 * ClassName &ConnectionInfListener
 * @author 吴向会
 * @d2016年8月2日
 */
public abstract interface ConnectionInfListener{
	/**
	 * 接收到消息,并分发到对应的
	 * @param codecData
	 */
	abstract void onReceiveMessage(CMDCodec codecData);
	/**
	 * 关闭连接
	 * @return
	 */
	abstract void onCloseConnection();
	/**
	 * 连接是否活跃有效的
	 * @return
	 */
	abstract boolean isActive();

}
