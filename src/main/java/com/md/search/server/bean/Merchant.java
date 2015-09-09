package com.md.search.server.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户信息
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午4:44:55
 */
public class Merchant implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7511654544261927922L;

	/**
	 * 商户id
	 */
	private String id;

	/**
	 * 分类id
	 */
	private String sortId;

	/**
	 * 商户名称
	 */
	private String title;

	/**
	 * 商户英文名称
	 */
	private String eTitle;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 手机号
	 */
	private String tel;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 图片地址
	 */
	private String picUrl;

	/**
	 * 头图
	 */
	private String headPic;

	/**
	 * 地图图片
	 */
	private String mapPicUrl;

	/**
	 * 启送价格
	 */
	private int limitPrice;

	/**
	 * 配送价格
	 */
	private int deliveryPrice;

	/**
	 * 业务说明
	 */
	private String description;

	/**
	 * 营业时间
	 */
	private String businessHours;

	private String lon;

	private String lat;

	/**
	 * 1 货到付款 2线上支付 9全部
	 */
	private int payType;

	/**
	 * 一个月订单量
	 */
	private int recentOrderCount;

	/**
	 * 一个月评分平均
	 */
	private int avgGrade;

	private Date addTime;

	/**
	 * 商户评级， S,A,B,C
	 */
	private String level_mark;

	/**
	 * 服务区域
	 */
	private String serviceArea;

	/**
	 * private 区块个数
	 */
	private int areas;

	/**
	 * 哪个区块
	 */
	private int which;

	/**
	 * 多边形
	 */
	private String geoMetry;

	public String getId() {
		return id;
	}

	public String getSortId() {
		return sortId;
	}

	public String getTitle() {
		return title;
	}

	public String geteTitle() {
		return eTitle;
	}

	public String getAddress() {
		return address;
	}

	public String getTel() {
		return tel;
	}

	public String getPhone() {
		return phone;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public String getHeadPic() {
		return headPic;
	}

	public String getMapPicUrl() {
		return mapPicUrl;
	}

	public int getLimitPrice() {
		return limitPrice;
	}

	public int getDeliveryPrice() {
		return deliveryPrice;
	}

	public String getDescription() {
		return description;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public String getLon() {
		return lon;
	}

	public String getLat() {
		return lat;
	}

	public int getPayType() {
		return payType;
	}

	public int getRecentOrderCount() {
		return recentOrderCount;
	}

	public int getAvgGrade() {
		return avgGrade;
	}

	public Date getAddTime() {
		return addTime;
	}

	public String getLevel_mark() {
		return level_mark;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void seteTitle(String eTitle) {
		this.eTitle = eTitle;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public void setMapPicUrl(String mapPicUrl) {
		this.mapPicUrl = mapPicUrl;
	}

	public void setLimitPrice(int limitPrice) {
		this.limitPrice = limitPrice;
	}

	public void setDeliveryPrice(int deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public void setRecentOrderCount(int recentOrderCount) {
		this.recentOrderCount = recentOrderCount;
	}

	public void setAvgGrade(int avgGrade) {
		this.avgGrade = avgGrade;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public void setLevel_mark(String level_mark) {
		this.level_mark = level_mark;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public int getAreas() {
		return areas;
	}

	public void setAreas(int areas) {
		this.areas = areas;
	}

	public int getWhich() {
		return which;
	}

	public void setWhich(int which) {
		this.which = which;
	}

	public String getGeoMetry() {
		return geoMetry;
	}

	public void setGeoMetry(String geoMetry) {
		this.geoMetry = geoMetry;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
