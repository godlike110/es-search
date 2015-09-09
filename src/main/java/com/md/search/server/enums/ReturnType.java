package com.md.search.server.enums;

/**
 * 搜索返回状态
 * 
 * @author zhiwei.wen
 * @Date 2015年8月20日 下午2:47:45
 */
public enum ReturnType {

	SUCCESS(0, "success"), FAILD(1, "failed");

	private int type;
	private String msg;

	ReturnType(int type, String msg) {
		this.type = type;
		this.msg = msg;
	}

	public int getType() {
		return type;
	}

	public String getMsg() {
		return msg;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
