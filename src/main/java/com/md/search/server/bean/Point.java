package com.md.search.server.bean;

import java.io.Serializable;

/**
 * 位置
 * 
 * @author zhiwei.wen
 * @Date 2015年8月18日 上午9:33:26
 */
public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4606596408799951305L;

	private Double lon;

	private Double lat;

	public Double getLon() {
		return lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

}
