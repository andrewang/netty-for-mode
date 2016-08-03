package com.aries.network.common;
/**
 * 模式类型枚举
 * ClassName &ModeType
 * @author 吴向会
 * @d2016年8月3日
 */
public class ModeType {
	/**
	 * 命令模式
	 */
	public static final int COMMAND_TYPE_VALUE=0;
	/**
	 * 命令模式字符串枚举
	 */
	private static final String COMMAND_TYPE="COMMAND_TYPE";
	/**
	 * 获取对应int类型的字符串枚举值，如果没有则返回null
	 * @param type
	 * @return
	 */
	public static String valueOf(int type){
		if(COMMAND_TYPE_VALUE==type){
			return COMMAND_TYPE;
		}
		return null;
	}
}
