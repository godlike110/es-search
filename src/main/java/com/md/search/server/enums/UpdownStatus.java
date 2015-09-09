package com.md.search.server.enums;

/**
 * 小区状态
 * 
 * @author zhiwei.wen
 * @Date 2015年8月10日 下午7:44:30
 */
public enum UpdownStatus {

	ENABLE(0, "enable"), DISABLE(1, "disable");

	private int status;

	private String msg;

	UpdownStatus(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
