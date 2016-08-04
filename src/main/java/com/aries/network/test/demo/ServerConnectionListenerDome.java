package com.aries.network.test.demo;

import com.aries.network.codec.CodeData;
import com.aries.network.connection.ConnectionInfListener;
import com.aries.network.connection.NettyConnection;
/**
 * 对服务器以客户端单个连接的监听.包括监听接收到的消息，和连接断开的通知
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
	public void onReceiveMessage(CodeData codecData) {
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
		CodeData sendCodecData= new CodeData(22, datas.getBytes());
		nCon.sendCommand(sendCodecData);
	}
	public void onCloseConnection() {
		System.out.println("连接断开。。。 IP: "+nCon.getRemoteIPAndPort());
	}
	public boolean isActive() {
		return nCon.getConnectionContext().channel().isActive();
	}
}
