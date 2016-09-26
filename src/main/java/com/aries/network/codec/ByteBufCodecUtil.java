package com.aries.network.codec;

import com.aries.network.common.CommonType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
/**
 * ByteBuf的解码编码器
 * ClassName &CodecUtil
 * @author 吴向会
 * @d2016年8月4日
 */
public class ByteBufCodecUtil {
	/**
	 * data数据的最大长度
	 */
	private static long maxDataLength =1024*1024;
	/**
	 * 对CodeData数据类型的解码,byte[]的长度大于了 maxDataLength返回null
	 * @param buf
	 * @return CodeData
	 */
	public static CodeData codeDataDecode(ByteBuf buf){
		if(buf.isReadable()){
			int length =buf.readInt();
    		int code =buf.readInt();
    		byte[] data =ByteBufUtil.getBytes(buf, buf.readerIndex(), buf.readableBytes());
    		if(data.length<=maxDataLength-CommonType.INT_BYTES_SIZE){
    			CodeData codecData = new CodeData(code, data);
    			return codecData;
    		}
    		System.out.println("data length: "+length);
    	}
		return null;
	}
	/**
	 * 对CodeData数据类型的解码,如果codeData  byte[]的长度大于了 maxDataLength返回null
	 * @param buf
	 * @return ByteBuf
	 */
	public static ByteBuf codeDataEncode(CodeData codeData){
		int bytesLength=CommonType.INT_BYTES_SIZE*2 + codeData.getData().length;
		if(bytesLength>maxDataLength){
			return null;
		}
		ByteBuf in = Unpooled.buffer(bytesLength);
		in.writeInt(bytesLength);
		in.writeInt(codeData.getCommandID());
		in.writeBytes(codeData.getData());
		return in;
	}
	/**
	 * 数据的最大长度
	 * @return
	 */
	public static long getMaxDataLength() {
		return maxDataLength;
	}

}
