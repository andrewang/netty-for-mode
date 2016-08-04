package com.aries.network.test.demo;

import io.netty.channel.ChannelHandlerContext;
import com.aries.network.connection.ConnectionInfListener;
import com.aries.network.connection.NettyConnection;
import com.aries.network.server.NetServer;
import com.aries.network.server.AbstractNetManagerInf;
/**
 * 服务器测试demo类，单列
 * ClassName &ServerTest
 * @author 吴向会
 * @d2016年8月1日
 */
public class ServerTestDemo implements AbstractNetManagerInf{
	private NetServer netServer=null;
	private static ServerTestDemo _ServerTest;
	/**
	 * 建立服务器服务时，使用单列模式
	 * @return
	 */
	public static ServerTestDemo getInstance() {
		if (_ServerTest == null) {
			_ServerTest = new ServerTestDemo();
		}
		return _ServerTest;
	}
	private ServerTestDemo(){
		netServer=new NetServer(54564, this);
	}
	public static void main(String[] args) {
		ServerTestDemo.getInstance().netServer.start();
	}
	public ConnectionInfListener registerConNotify(ChannelHandlerContext connection) {
		System.out.println("服务端开放连接.....");
		System.out.println("开放IP："+connection.channel().remoteAddress());
		System.out.println("ChannelId: "+connection.channel().id().asLongText());
		System.out.println("connection: "+connection);
		return new ServerConnectionListenerDome(new NettyConnection(connection)) ;
	}
}
