package com.aries.network.test.demo;

import io.netty.channel.ChannelHandlerContext;

import com.aries.network.client.AbstractClientManagerInf;
import com.aries.network.client.NettyClient;
import com.aries.network.codec.CodeData;
import com.aries.network.connection.NettyConnection;
/**
 * 客户端连接测试demo类
 * ClassName &ClientTest
 * @author 吴向会
 * @d2016年8月1日
 */
public class ClientTestDemo implements AbstractClientManagerInf {
	private NettyClient _client=null;
	private NettyConnection nCon=null;
	private int count=0;
	public static ClientTestDemo _ClientTest;
	private ClientTestDemo(){
		_client=new NettyClient(this);
	}
	public static ClientTestDemo getInstance(){
		if (_ClientTest == null) {
			_ClientTest = new ClientTestDemo();
		}
		return _ClientTest;
	}
	public static void main(String[] args) {
		 new Thread(new Runnable() {
				public void run() {
					boolean isSend=false;
					int time=0;
					while(true){
						 try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						 if(ClientTestDemo.getInstance().nCon.getConnectionContext().channel().isActive()&&!isSend){
								String datas =ClientTestDemo.getInstance().nCon.getRemoteIPAndPort()+"\t"+ClientTestDemo.getInstance().count+":\t客户端发送消息测试";
								CodeData sendCodecData= new CodeData(22, datas.getBytes());
								ClientTestDemo.getInstance().nCon.sendCommand(sendCodecData);
								isSend=true;
						 }
						 time+=4000;
						 if(time>=20000){
							/*// ClientTestDemo.getInstance().nCon.getConnectionContext().disconnect();
							 ClientTestDemo.getInstance().nCon.closeConnection();
							 System.out.println("客户端主动关闭。。");*/
							 break;
						 }
					}
				}
			},"server stop").start();
		ClientTestDemo.getInstance()._client.connect(54564, "localhost");
		
	}

	public void closeConNotify() {
		System.out.println("客户端断开连接....");
		
	}

	public void registerConNotify(ChannelHandlerContext connection) {
		System.out.println("客户端开放连接.....");
		ClientTestDemo.getInstance().nCon= new NettyConnection(connection);
		System.out.println("ChannelId: "+connection.channel().id().asLongText());
		System.out.println("connection: "+connection);
		
	}

	public void receiveMessage(CodeData codecData) {
		System.out.println("接收到消息");
		System.out.println("client_codeId: "+codecData.getCommandID());
		System.out.println("client_data: "+new String( codecData.getData()));
		count++;
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String datas =nCon.getRemoteIPAndPort()+"\t"+count+":\t客户端发送消息测试";
		CMDCodec sendCodecData= new CMDCodec(22, datas.getBytes());
		nCon.sendCommand(sendCodecData);*/
	}
}
