package com.aries.network.handler;

import com.aries.network.codec.ByteBufCodecUtil;
import com.aries.network.codec.CodeData;
import com.aries.network.connection.NetConnectionInf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
/**
 * 命令模式handler
 * ClassName &CommandServerHandler
 * @author 吴向会
 * @d2016年7月28日
 */

public class CommandServerHandler extends ChannelInboundHandlerAdapter   {
	/**
	 * 抽象化连接类，用于通知服务（NetServer 或 NettyClient）
	 */
	private NetConnectionInf abstractConnection=null;
	public CommandServerHandler(NetConnectionInf abstractConnection){
		this.abstractConnection=abstractConnection;
	}
	/**
	 * 接收到客户端数据是
	 */
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	    try {
	    	CodeData codecData = ByteBufCodecUtil.codeDataDecode((ByteBuf) msg);
    		if(abstractConnection!=null){
    			abstractConnection.receiveMessage(ctx.channel().id(),codecData);
    		}
	    } finally {
	        ReferenceCountUtil.release(msg);
	    }

	}
	/**
	 * 异常时通知服务管理器，通知服务器执行相应动作并执行断开连接
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(abstractConnection!=null){
			abstractConnection.closeConNotify(ctx.channel().id());
		}
		System.out.println("exceptionCaught Exception");
		//cause.printStackTrace();
        ctx.close();
	}
	/**
	 * 注册时执行通知，通知服务管理器,注册连接通道的上下文对象
	 */
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		if(abstractConnection!=null){
			abstractConnection.registerConNotify(ctx);
		}
	}
	/**
	 * 接收到该连接将要关闭
	 */
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        abstractConnection.closeConNotify(ctx.channel().id());
    }
}
