package com.aries.network.codec;
import io.netty.buffer.ByteBuf;

/**
 * 命令模式下的编码解码器
 * ClassName &CMDCodec
 * @author 吴向会
 * @d2016年7月28日
 */
public class CodeData {
	/**
	 * 命令id
	 */
	private int commandID;
	/**
	 * btye[]数据
	 */
	private byte[] data;
	
	public CodeData(int commandID,byte[] data){
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
	 * 转换为ByteBuf,超过 ByteBufCodecUtil.getMaxDataLength()，返回null
	 * @return
	 */
	public ByteBuf toByteBuf() {
		return ByteBufCodecUtil.codeDataEncode(this);
	}

}
