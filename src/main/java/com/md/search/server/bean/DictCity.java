package com.md.search.server.bean;

import java.io.Serializable;

/**
 * 城市信息
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午1:46:45
 */
public class DictCity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6563973540482376123L;

	private String id;

	private String name;

	private String province;

	private String areaCode;

	private String voucherRule;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getProvince() {
		return province;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getVoucherRule() {
		return voucherRule;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setVoucherRule(String voucherRule) {
		this.voucherRule = voucherRule;
	}

}
