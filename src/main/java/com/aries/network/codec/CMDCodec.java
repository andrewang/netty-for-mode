package com.aries.network.codec;

import com.aries.network.common.CommonType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 命令模式下的编码解码器
 * ClassName &CMDCodec
 * @author 吴向会
 * @d2016年7月28日
 */
public class CMDCodec {
	/**
	 * 命令id
	 */
	private int commandID;
	/**
	 * btye[]数据
	 */
	private byte[] data;
	
	public CMDCodec(int commandID,byte[] data){
		this.commandID=commandID;
		this.data=data;
	}
	/**
	 * 获取命令id
	 * @return
	 */
	public int getCommandID() {
		return commandID;
	}
	/**
	 * 获取byte[]数据
	 * @return
	 */
	public byte[] getData() {
		return data;
	}
	/**
	 * 转换为ByteBuf
	 * @return
	 */
	public ByteBuf toByteBuf() {
		ByteBuf in = Unpooled.buffer(CommonType.INT_BYTES_SIZE + data.length);
		in.writeInt(30);
		in.writeBytes(data);
		return in;
	}

}
