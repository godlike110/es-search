package com.md.search.server.enums;

/**
 * 美到索引类型
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 上午11:19:00
 */
public enum MaydoType {

	WEIXINACCOUNT(1, "community"), DATARESOURCE(2, "merchant"), DATAUSER(3,
			"cuser"), MALLORDER(4, "mallorder");

	private int type;
	private String name;

	MaydoType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

}
