package com.aries.network.test.demo;

import com.aries.network.codec.CMDCodec;
import com.aries.network.connection.ConnectionInfListener;
import com.aries.network.connection.NettyConnection;
/**
 * 该connection仅做参考,未封装的原因是，让服务器对连接相关的操作更灵活。
 * ClassName &ServerConnection
 * @author 吴向会
 * @d2016年8月1日
 */
public class ServerConnectionListenerDome implements ConnectionInfListener {
	private int count=0;
	private NettyConnection nCon=null;
	public ServerConnectionListenerDome(NettyConnection nCon){
		this.nCon=nCon;
	}
	public void onReceiveMessage(CMDCodec codecData) {
		System.out.println("接收到消息");
		System.out.println("server_codeId: "+codecData.getCommandID());
		System.out.println("server_data: "+new String( codecData.getData()));
		count++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String datas =nCon.getRemoteIPAndPort()+"\t"+count+":\t服务器发送消息测试";
		CMDCodec sendCodecData= new CMDCodec(22, datas.getBytes());
		nCon.sendCommand(sendCodecData);
	}
	public void onCloseConnection() {
		System.out.println("连接断开。。。 IP: "+nCon.getRemoteIPAndPort());
	}
	public boolean isActive() {
		return nCon.getConnectionContext().channel().isActive();
	}
}
