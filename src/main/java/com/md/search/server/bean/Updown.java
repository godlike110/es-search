package com.md.search.server.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 小区类
 * 
 * @author zhiwei.wen
 * @Date 2015年8月5日 下午3:05:49
 */
public class Updown implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6669324638171770028L;

	private String id;

	/**
	 * 小区名称
	 */
	private String name;

	/**
	 * 小区城市
	 */
	private String tableFlx;

	/**
	 * 经度
	 */
	private String lon;

	/**
	 * 纬度
	 */
	private String lat;

	private String region;

	private String city;

	private String dist;

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 小区评级
	 */
	private String levelMark;

	/**
	 * 地区
	 */
	private String area1;

	/**
	 * 开发商
	 */
	private String developer;

	/**
	 * 物业管理公司
	 */
	private String managementCompany;

	/**
	 * 住宅类型
	 */
	private String managementType;

	/**
	 * 物业管理费用
	 */
	private String managementFee;

	/**
	 * 总面积
	 */
	private String totalArea;

	/**
	 * 总户数
	 */
	private String totalNumber;

	/**
	 * 建设年代
	 */
	private String builtIn;

	/**
	 * 容积率
	 */
	private String lettingRate;

	/**
	 * 价格
	 */
	private String price;

	/**
	 * 是否上线
	 */
	private int isOnline;

	/**
	 * 是否禁用 1表示禁用
	 */
	private int isDisable;

	private Date updateTime;

	/**
	 * 订单量
	 */
	private int resourceOrderCount;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTableFlx() {
		return tableFlx;
	}

	public String getLon() {
		return lon;
	}

	public String getLat() {
		return lat;
	}

	public String getAddress() {
		return address;
	}

	public String getArea1() {
		return area1;
	}

	public String getDeveloper() {
		return developer;
	}

	public String getManagementCompany() {
		return managementCompany;
	}

	public String getManagementType() {
		return managementType;
	}

	public String getManagementFee() {
		return managementFee;
	}

	public String getTotalArea() {
		return totalArea;
	}

	public String getTotalNumber() {
		return totalNumber;
	}

	public String getBuiltIn() {
		return builtIn;
	}

	public String getLettingRate() {
		return lettingRate;
	}

	public String getPrice() {
		return price;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTableFlx(String tableFlx) {
		this.tableFlx = tableFlx;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setArea1(String area1) {
		this.area1 = area1;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public void setManagementCompany(String managementCompany) {
		this.managementCompany = managementCompany;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	public void setManagementFee(String managementFee) {
		this.managementFee = managementFee;
	}

	public void setTotalArea(String totalArea) {
		this.totalArea = totalArea;
	}

	public void setTotalNumber(String totalNumber) {
		this.totalNumber = totalNumber;
	}

	public void setBuiltIn(String builtIn) {
		this.builtIn = builtIn;
	}

	public void setLettingRate(String lettingRate) {
		this.lettingRate = lettingRate;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public String getRegion() {
		return region;
	}

	public String getCity() {
		return city;
	}

	public String getDist() {
		return dist;
	}

	public String getLevelMark() {
		return levelMark;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public void setLevelMark(String levelMark) {
		this.levelMark = levelMark;
	}

	public int getResourceOrderCount() {
		return resourceOrderCount;
	}

	public void setResourceOrderCount(int resourceOrderCount) {
		this.resourceOrderCount = resourceOrderCount;
	}

	public int getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(int isDisable) {
		this.isDisable = isDisable;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
